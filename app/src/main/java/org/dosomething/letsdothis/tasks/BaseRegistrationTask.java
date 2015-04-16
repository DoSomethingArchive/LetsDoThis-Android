package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.apache.http.HttpStatus;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import co.touchlab.android.threading.tasks.Task;
import retrofit.RetrofitError;

/**
 * Created by toidiu on 4/16/15.
 */
public abstract class BaseRegistrationTask extends Task
{
    protected final String  email;
    protected final String  phone;
    protected final String  password;
    public        boolean success;

    protected BaseRegistrationTask(String email, String phone, String password)
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
        if(email==null && phone==null)
        {
            return;
        }

        attemptRegistration(context);
    }

    protected abstract void attemptRegistration(Context context) throws Throwable;

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

}
