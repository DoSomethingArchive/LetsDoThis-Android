package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.models.LoginResponse;

import java.net.HttpURLConnection;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.RetrofitError;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginTask extends BaseRegistrationTask
{

    public LoginTask(String email, String phone, String password)
    {
        super(email, phone, password);
    }

    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        LoginResponse response = null;
        if(email != null)
        {
            response = DataHelper.getNorthstarAPIService()
                    .loginWithEmail(email, password);
        }
        else if(phone != null)
        {
            response = DataHelper.getNorthstarAPIService()
                    .loginWithMobile(phone, password);
        }

        if(response != null)
        {
            if(response._id != null)
            {
                success = true;
            }
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        if(((RetrofitError) throwable).getResponse()
                .getStatus() == HttpURLConnection.HTTP_PRECON_FAILED)
        {
            return true;
        }
        return super.handleError(context, throwable);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
