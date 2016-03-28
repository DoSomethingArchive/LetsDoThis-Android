package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Task to get a user's profile details.
 *
 * Created by toidiu on 4/16/15.
 */
public class GetProfileTask extends BaseNetworkErrorHandlerTask {

    // User's Northstar ID, if any
    private String mUserId;

    // User result from network response
    private User mResult;

    public GetProfileTask() {
        mUserId = null;
    }

    public GetProfileTask(String id) {
        mUserId = id;
    }

    @Override
    protected void run(Context context) throws Throwable {
        NorthstarAPI northstar = NetworkHelper.getNorthstarAPIService();
        // Get profile of the logged-in user
        if (mUserId == null) {
            AppPrefs appPrefs = AppPrefs.getInstance(context);
            String token = appPrefs.getSessionToken();
            ResponseUser response = northstar.userProfile(token);

            mResult = ResponseUser.getUser(response);

            DatabaseHelper.getInstance(context).getUserDao().createOrUpdate(mResult);
        }
        // Get profile of some other user
        else {
            ResponseUser response = northstar.getUserById(String.valueOf(mUserId));

            mResult = ResponseUser.getUser(response);
        }
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

    public User getResult() {
        return mResult;
    }
}
