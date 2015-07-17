package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseGroupList;
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
public class GetCurrentUserCampaignsTask extends BaseNetworkErrorHandlerTask
{
    public List<Campaign> currentCampaignList;
    public List<Campaign> pastCampaignList;

    public GetCurrentUserCampaignsTask()
    {
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        NorthstarAPI northstarAPIService = NetworkHelper.getNorthstarAPIService();
        ResponseUserCampaign userCampaigns = northstarAPIService
                .getUserCampaigns(AppPrefs.getInstance(context).getCurrentUserId());


        String currentUserId = AppPrefs.getInstance(context).getCurrentUserId();
        getCurrentCampaign(northstarAPIService, userCampaigns, currentUserId);
        getPastCampaign(context, userCampaigns);
    }

    private void getCurrentCampaign(NorthstarAPI northstarAPIService, ResponseUserCampaign userCampaigns, String currentUserId) throws NetworkException
    {
        currentCampaignList = new ArrayList<>();
        ArrayList<Integer> doneCampaigns = new ArrayList<Integer>();

        //-------get user's campaign id list/ which ones have RB
        String campaignIds = "";
        String signUpGroups = "";
        for(ResponseUserCampaign.Wrapper campaignData : userCampaigns.data)
        {
            if(campaignData.reportback_data != null)
            {
                doneCampaigns.add(campaignData.drupal_id);
            }
            campaignIds += campaignData.drupal_id + ",";
            signUpGroups += campaignData.signup_group + ",";
        }

        //-------get campaign and mark which have RB
        Map<Integer, Campaign> campMap = new HashMap<>();
        {
            ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                    .campaignListByIds(campaignIds);
            List<Campaign> campaigns = ResponseCampaignList.getCampaigns(responseCampaignList);

            for(Campaign campaign : campaigns)
            {
                if(doneCampaigns.contains(campaign.id))
                {
                    campaign.showShare = Campaign.UploadShare.SHARE;
                }
                campMap.put(campaign.id, campaign);
            }
        }

        //-------add group info for the campaign
        signUpGroups = signUpGroups.substring(0, signUpGroups.length() - 1);
        ResponseGroupList response = northstarAPIService.groupList(signUpGroups);
        ResponseGroupList.addUsers(campMap, response, currentUserId);

        currentCampaignList.addAll(campMap.values());
    }

    private void getPastCampaign(Context context, ResponseUserCampaign userCampaigns) throws java.sql.SQLException, NetworkException
    {
        pastCampaignList = new ArrayList<>();
        List<Integer> currCampIds = new ArrayList<>();

        List<Campaign> currCamp = DatabaseHelper.getInstance(context).getCampDao().queryForAll();
        for(Campaign camp : currCamp)
        {
            currCampIds.add(camp.id);
        }

        String pastIds = "";
        for(ResponseUserCampaign.Wrapper campaignData : userCampaigns.data)
        {
            if(currCampIds.contains(campaignData.drupal_id))
            {
                pastIds = campaignData.drupal_id + ",";
            }
        }
        pastIds = pastIds.substring(0, pastIds.length() - 1);


        if(! pastIds.isEmpty())
        {
            ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                    .campaignListByIds(pastIds);
            pastCampaignList = ResponseCampaignList.getCampaigns(responseCampaignList);
        }
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
