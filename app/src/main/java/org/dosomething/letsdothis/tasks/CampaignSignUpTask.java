package org.dosomething.letsdothis.tasks;
import android.content.Context;
import android.util.Log;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.RequestCampaignSignup;
import org.dosomething.letsdothis.network.models.ResponseCampaignSignUp;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 6/26/15.
 */
//todo should this be persisted
public class CampaignSignUpTask extends Task
{
    private int campaignId;

    public CampaignSignUpTask(int campaignId)
    {
        this.campaignId = campaignId;
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();
        RequestCampaignSignup requestCampaignSignup = new RequestCampaignSignup();
        ResponseCampaignSignUp response = NetworkHelper.getDoSomethingAPIService()
                                                       .campaignSignUp(requestCampaignSignup,
                                                                       campaignId, sessionToken);
        Log.d("boo", String.valueOf(ResponseCampaignSignUp.getSignUpId(response)));
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }
}
