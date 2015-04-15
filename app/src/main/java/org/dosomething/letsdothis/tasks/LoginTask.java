package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.apache.http.HttpStatus;
import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginTask extends Task
{
    private final String  email;
    private final String  phone;
    private final String  password;
    public        boolean success;

    public LoginTask(String email, String phone, String password)
    {
        this.email = email.isEmpty()
                ? null
                : email;
        this.phone = phone.isEmpty()
                ? null
                : phone;
        this.password = password;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        Response response = null;
        if(email != null)
        {
            response = DataHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .loginWithEmail(email, password);
            DataHelper.debugOut(response);
        }
        else if(phone != null)
        {
            response = DataHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .loginWithMobile(phone, password);
        }

        if(response != null)
        {
            success = true;
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        if(((RetrofitError) throwable).getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED)
        {
            return true;
        }
        if(throwable
                .getCause() instanceof NetworkException || throwable instanceof NetworkException)
        {
            return true;
        }
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
