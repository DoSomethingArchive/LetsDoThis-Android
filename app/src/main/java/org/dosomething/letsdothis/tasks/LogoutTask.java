package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.facebook.login.LoginManager;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class LogoutTask extends BaseNetworkErrorHandlerTask {

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);

        EventBusExt.getDefault().post(this);
    }

    @Override
    protected void run(Context context) throws Throwable {
        LoginManager.getInstance().logOut();

        // Get current token to logout with
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();

        // Clear local cache of the token
        AppPrefs.getInstance(context).logout();

        // Logout from API
        NetworkHelper.getNorthstarAPIService().logout(sessionToken);
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        return true;
    }
}
