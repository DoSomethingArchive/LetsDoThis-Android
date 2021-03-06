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
import co.touchlab.android.threading.tasks.utils.NetworkUtils;

/**
 * Task to submit a campaign signup for the logged in user.
 *
 * Created by izzyoji :) on 6/26/15.
 */
public class CampaignSignUpTask extends Task {
    // Campaign ID the signup is for
    private int mCampaignId;

    // Pager position the signup came from
    private int mPagerPosition;

    // Set to true if an error occurs
    private boolean mHasError;

    // Set to true if this is a new signup
    private boolean mIsNewSignup;

    public CampaignSignUpTask(int campaignId) {
        this.mCampaignId = campaignId;
        this.mIsNewSignup = false;
    }

    public CampaignSignUpTask(int campaignId, int pagerPosition) {
        this.mCampaignId = campaignId;
        this.mPagerPosition = pagerPosition;
        this.mHasError = false;
        this.mIsNewSignup = false;
    }

    //
    // Getters
    //
    public int getCampaignId() {
        return mCampaignId;
    }

    public int getPagerPosition() {
        return mPagerPosition;
    }

    public boolean hasError() {
        return mHasError;
    }

    public boolean isNewSignup() {
        return mIsNewSignup;
    }

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }

    @Override
    protected void run(Context context) throws Throwable {
        if (!NetworkUtils.isOnline(context)) {
            throw new Exception();
        }

        CampaignActions actions = CampaignActions.queryForId(context, mCampaignId);
        if (actions == null || actions.signUpId <= 0) {
            String sessionToken = AppPrefs.getInstance(context).getSessionToken();
            RequestCampaignSignup signup= new RequestCampaignSignup();
            signup.campaign_id = mCampaignId;

            ResponseCampaignSignUp response = NetworkHelper.getNorthstarAPIService()
                    .submitSignUp(sessionToken, signup);

            CampaignActions campaignActions = new CampaignActions();
            campaignActions.campaignId = mCampaignId;
            campaignActions.signUpId = ResponseCampaignSignUp.getSignUpId(response);
            CampaignActions.save(context, campaignActions);

            this.mIsNewSignup = true;
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        Toast.makeText(context, R.string.error_signup, Toast.LENGTH_SHORT).show();
        mHasError = true;
        return true;
    }
}
