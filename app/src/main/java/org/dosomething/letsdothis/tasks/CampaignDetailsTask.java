package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaign;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignDetailsTask extends BaseNetworkErrorHandlerTask
{
    private final int                             campaignId;
    public        Campaign                        campaign;

    public CampaignDetailsTask(int campaignId)
    {
        this.campaignId = campaignId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseCampaignWrapper response = NetworkHelper.getPhoenixAPIService()
                .campaign(campaignId);
        campaign = ResponseCampaign.getCampaign(response.data);

        String currentUserId = AppPrefs.getInstance(context).getCurrentUserId();
        ResponseUserCampaign userCampaigns = NetworkHelper.getNorthstarAPIService()
                .getUserCampaigns(currentUserId);
        for(ResponseUserCampaign.Wrapper c : userCampaigns.data)
        {
            if(campaignId == c.drupal_id)
            {
                campaign.signupGroup = c.signup_group;
                if(c.reportback_data != null)
                {
                    campaign.showShare = Campaign.UploadShare.SHARE;
                    return;
                }
            }
        }
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
