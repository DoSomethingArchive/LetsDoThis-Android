package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.ArrayList;
import java.util.List;

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
        currentCampaignList = new ArrayList<>();
        ArrayList<Integer> doneCampaigns = new ArrayList<Integer>();
        String currentUserId = AppPrefs.getInstance(context).getCurrentUserId();
        ResponseUserCampaign userCampaigns = NetworkHelper.getNorthstarAPIService()
                .getUserCampaigns(currentUserId);

        String campaignIds = "";
        for(ResponseUserCampaign.Wrapper campaignData : userCampaigns.data)
        {
            if(campaignData.reportback_data != null)
            {
                doneCampaigns.add(campaignData.drupal_id);
            }
            campaignIds += campaignData.drupal_id + ",";
        }

        ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                .campaignListByIds(campaignIds);
        List<Campaign> campaigns = ResponseCampaignList.getCampaigns(responseCampaignList);

        for(int i = 0; i < campaigns.size(); i++)
        {
            Campaign campaign = campaigns.get(i);
            if(doneCampaigns.contains(campaign.id))
            {
                campaign.showShare = Campaign.UploadShare.SHARE;
                campaigns.set(i, campaign);
            }
        }

        currentCampaignList.addAll(campaigns);
        //FIXME get friend group
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
