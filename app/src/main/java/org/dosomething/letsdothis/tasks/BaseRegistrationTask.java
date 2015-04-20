package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.apache.http.HttpStatus;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import co.touchlab.android.threading.tasks.Task;
import retrofit.RetrofitError;

/**
 * Created by toidiu on 4/16/15.
 */
public abstract class BaseRegistrationTask extends BaseNetworkErrorHandlerTask
{
    protected final String  email;
    protected final String  phone;
    protected final String  password;

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

    protected void loginUser(Context context, User user) throws Throwable
    {
        DatabaseHelper.getInstance(context).getUserDao().createOrUpdate(user);
        AppPrefs.getInstance(context).setCurrentUserId(user.id);
    }

    public  static void logout(Context context)
    {
        AppPrefs.getInstance(context).logout();
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

}
