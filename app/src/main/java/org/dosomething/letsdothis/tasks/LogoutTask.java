package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class LogoutTask extends BaseNetworkErrorHandlerTask
{
    private String sessionToken;

    @Override
    protected void run(Context context) throws Throwable
    {
        //FIXME switch to using persisted task
        LDTApplication.loginManager.logOut();
        EventBusExt.getDefault().post(this);

        sessionToken = AppPrefs.getInstance(context).getSessionToken();
        NetworkHelper.getNorthstarAPIService().logout(sessionToken);
        AppPrefs.getInstance(context).logout();
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return true;
    }
}
