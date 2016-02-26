package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.CampaignActions;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.UserReportBack;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetUserCampaignsTask extends BaseNetworkErrorHandlerTask {
    // User id we're getting campaigns info for
    private String mUserId;

    // Originating class this task came from
    private String mOrigin;

    // Campaigns a user is participating in
    public List<Campaign> currentCampaignList;

    // Campaigns that have ended that a user participated in
    public List<Campaign> pastCampaignList;

    public GetUserCampaignsTask(String id) {
        mUserId = id;
        mOrigin = null;
    }

    public GetUserCampaignsTask(String id, String origin) {
        mUserId = id;
        mOrigin = origin;
    }

    public String getOrigin() {
        return mOrigin;
    }

    @Override
    protected void run(Context context) throws Throwable {
        NorthstarAPI northstarAPIService = NetworkHelper.getNorthstarAPIService();
        ResponseUserCampaign userCampaigns = northstarAPIService.getUserCampaigns(mUserId);

        getCurrentCampaign(userCampaigns);
        getPastCampaign(context, userCampaigns);

        updateCurrentUserActions(context, userCampaigns);
    }

    private void getCurrentCampaign(ResponseUserCampaign userCampaigns) throws NetworkException {
        currentCampaignList = new ArrayList<>();

        // Key: campaign id. Value: user's reportback data
        Map<Integer, UserReportBack> reportBacks = new HashMap<>();

        // Comma-separated string of campaign ids the user is at least signed up for
        String campaignIds = "";

        for (ResponseUserCampaign.Wrapper campaignData : userCampaigns.data) {
            // Build query string for campaigns the user's signed up for
            campaignIds += campaignData.drupal_id + ",";

            // Cache off reportback data when available
            if (campaignData.reportback_data != null) {
                UserReportBack userReportBack = new UserReportBack();
                userReportBack.id = campaignData.reportback_data.id;
                userReportBack.quantity = campaignData.reportback_data.quantity;

                for (ReportBack rb : campaignData.reportback_data.reportback_items.data) {
                    userReportBack.addItem(String.valueOf(rb.id), rb.caption, rb.getImagePath());
                }

                reportBacks.put(campaignData.drupal_id, userReportBack);
            }
        }

        // Populate the currentCampaigns list
        if (!campaignIds.isEmpty()) {
            ResponseCampaignList responseCampaignList = NetworkHelper.getPhoenixAPIService()
                    .campaignListByIds(campaignIds);
            List<Campaign> campaigns = responseCampaignList.getCampaigns(true);

            for (Campaign campaign : campaigns) {
                // Add reportback data, if any
                if (reportBacks.containsKey(campaign.id)) {
                    campaign.showShare = Campaign.UploadShare.SHARE;
                    campaign.userReportBack = reportBacks.get(campaign.id);
                }

                currentCampaignList.add(campaign);
            }
        }
    }

    private void getPastCampaign(Context context, ResponseUserCampaign userCampaigns) throws java.sql.SQLException, NetworkException
    {
        pastCampaignList = new ArrayList<>();
        List<Integer> currCampIds = new ArrayList<>();

        List<Campaign> currCamp = DatabaseHelper.getInstance(context).getCampDao().queryForAll();
        for (Campaign camp : currCamp)
        {
            currCampIds.add(camp.id);
        }

        String pastIds = "";
        for (ResponseUserCampaign.Wrapper campaignData : userCampaigns.data)
        {
            if (!currCampIds.contains(campaignData.drupal_id))
            {
                pastIds = campaignData.drupal_id + ",";
            }
        }

        if (!pastIds.isEmpty())
        {
            pastIds = pastIds.substring(0, pastIds.length() - 1);
            ResponseCampaignList responseCampaignList = NetworkHelper.getPhoenixAPIService()
                    .campaignListByIds(pastIds);
            pastCampaignList = responseCampaignList.getCampaigns(false);
        }
    }

    /**
     * Updates the local cache of user actions if these results are for our current logged in user.
     *
     * @param context
     * @param userCampaigns
     */
    private void updateCurrentUserActions(Context context, ResponseUserCampaign userCampaigns) throws Throwable {
        if (!mUserId.equals(AppPrefs.getInstance(context).getCurrentUserId())) {
            return;
        }

        for (ResponseUserCampaign.Wrapper campaignData : userCampaigns.data) {
            CampaignActions actions = new CampaignActions();
            actions.campaignId = campaignData.drupal_id;
            actions.signUpId = campaignData.signup_id;
            if (campaignData.reportback_data != null) {
                actions.reportBackId = Integer.parseInt(campaignData.reportback_data.id);
            }

            CampaignActions.save(context, actions);
        }
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
