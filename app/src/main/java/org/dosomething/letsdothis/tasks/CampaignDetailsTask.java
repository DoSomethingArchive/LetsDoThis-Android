package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaign;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignDetailsTask extends BaseNetworkErrorHandlerTask
{
    private final int      campaignId;
    public        Campaign campaign;

    public CampaignDetailsTask(int campaignId)
    {
        this.campaignId = campaignId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {

        ResponseCampaignWrapper response = NetworkHelper.getDoSomethingAPIService().campaign(campaignId);
        campaign = ResponseCampaign.getCampaign(response.data);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
