package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.CampaignActions;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.RequestCampaignSignup;
import org.dosomething.letsdothis.network.models.ResponseCampaignSignUp;
import org.dosomething.letsdothis.utils.AppPrefs;

/**
 * Created by izzyoji :) on 7/10/15.
 */
public class JoinGroupTask extends BaseNetworkErrorHandlerTask
{
    private final int campaignId;
    private final int groupId;

    public JoinGroupTask(int campaignId, int groupId)
    {
        this.campaignId = campaignId;
        this.groupId = groupId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();
        RequestCampaignSignup requestCampaignSignup = new RequestCampaignSignup(groupId);
        ResponseCampaignSignUp response = NetworkHelper.getNorthstarAPIService().campaignSignUp(requestCampaignSignup,
                                                                           campaignId, sessionToken);

        CampaignActions campaignActions = new CampaignActions();
        campaignActions.campaignId = campaignId;
        campaignActions.signUpId = ResponseCampaignSignUp.getSignUpId(response);
        CampaignActions.save(context, campaignActions);

    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        return false;
    }
}
