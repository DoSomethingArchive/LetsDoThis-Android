package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
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

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetCurrentUserCampaignsTask extends BaseNetworkErrorHandlerTask
{
    public List<Campaign> currentCampaignList;

    public GetCurrentUserCampaignsTask()
    {
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ArrayList<Integer> doneCampaigns = new ArrayList<Integer>();
        NorthstarAPI northstarAPIService = NetworkHelper.getNorthstarAPIService();

        //-------get user's campaign id list/ which ones have RB
        ResponseUserCampaign userCampaigns = northstarAPIService
                .getUserCampaigns(AppPrefs.getInstance(context).getCurrentUserId());
        String campaignIds = "";
        String signupIds = "";
        for(ResponseUserCampaign.Wrapper campaignData : userCampaigns.data)
        {
            if(campaignData.reportback_data != null)
            {
                doneCampaigns.add(Integer.parseInt(campaignData.drupal_id));
            }
            campaignIds += campaignData.drupal_id + ",";
            signupIds += campaignData.signup_id + ",";
        }

        //-------get campaign and mark which have RB
        Map<Integer, Campaign> campMap = new HashMap<>();
        {
            ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                    .campaignListByIds(campaignIds);
            List<Campaign> campaigns = ResponseCampaignList.getCampaigns(responseCampaignList);

            for(Campaign c : campaigns)
            {
                if(doneCampaigns.contains(c))
                {
                    c.showShare = Campaign.UploadShare.SHARE;
                }
                campMap.put(c.id, c);
            }
        }

        //-------add group info for the campaign
        signupIds = signupIds.substring(0, signupIds.length() - 1);
        ResponseGroupList response = northstarAPIService.groupList(signupIds);
        ResponseGroupList.addUsers(campMap, response);


        currentCampaignList = new ArrayList<>(campMap.values());
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
