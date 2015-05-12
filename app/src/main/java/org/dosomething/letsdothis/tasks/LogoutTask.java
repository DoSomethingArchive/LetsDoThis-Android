package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.net.HttpURLConnection;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.client.Response;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class LogoutTask extends BaseNetworkErrorHandlerTask
{
    @Override
    protected void run(Context context) throws Throwable
    {
        AppPrefs.getInstance(context).logout();
        LDTApplication.loginManager.logOut();
        EventBusExt.getDefault().post(this);


        Response response = NetworkHelper.getNorthstarAPIService()
                                       .logout(AppPrefs.getInstance(context).getSessionToken());

        if(response != null && response.getStatus() == HttpURLConnection.HTTP_OK)
        {
            //MARKME we can potentially do some sort of check later or keep the session token if it fails
        }

    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }
}
