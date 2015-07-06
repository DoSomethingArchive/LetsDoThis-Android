package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;

import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public class InterestGroupCampaignListTask extends Task
{
    public int            interestGroupId;
    public List<Campaign> campaigns;

    public InterestGroupCampaignListTask(int interestGroupId)
    {
        this.interestGroupId = interestGroupId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseCampaignList response = NetworkHelper.getDoSomethingAPIService().campaignList(interestGroupId);
        campaigns = ResponseCampaignList.getCampaigns(response);
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

}
