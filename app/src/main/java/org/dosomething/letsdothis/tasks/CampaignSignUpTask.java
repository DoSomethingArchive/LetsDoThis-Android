package org.dosomething.letsdothis.tasks;
import android.content.Context;
import android.widget.Toast;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.CampaignActions;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.RequestCampaignSignup;
import org.dosomething.letsdothis.network.models.ResponseCampaignSignUp;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 6/26/15.
 */
//todo should this be persisted
public class CampaignSignUpTask extends Task {
    private int campaignId;

    private boolean hasError;

    public CampaignSignUpTask(int campaignId) {
        this.campaignId = campaignId;
        this.hasError = false;
    }

    //
    // Getters
    //
    public int getCampaignId() {
        return campaignId;
    }

    public boolean hasError() {
        return hasError;
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

    @Override
    protected void run(Context context) throws Throwable {
        CampaignActions actions = CampaignActions.queryForId(context, campaignId);
        if (actions == null || actions.signUpId <= 0) {
            String sessionToken = AppPrefs.getInstance(context).getSessionToken();
            RequestCampaignSignup requestCampaignSignup = new RequestCampaignSignup(null);
            ResponseCampaignSignUp response = NetworkHelper.getNorthstarAPIService()
                                                           .campaignSignUp(requestCampaignSignup,
                                                                           campaignId, sessionToken);
            CampaignActions campaignActions = new CampaignActions();
            campaignActions.campaignId = campaignId;
            campaignActions.signUpId = ResponseCampaignSignUp.getSignUpId(response);
            CampaignActions.save(context, campaignActions);
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        Toast.makeText(context, context.getString(R.string.error_signup), Toast.LENGTH_SHORT).show();
        hasError = true;
        return true;
    }
}
