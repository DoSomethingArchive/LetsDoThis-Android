package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toidiu on 4/16/15.
 */
public abstract class BaseRegistrationTask extends BaseNetworkErrorHandlerTask
{
    protected final String phoneEmail;
    protected final String password;

    protected BaseRegistrationTask(String phoneEmail, String password)
    {
        this.phoneEmail = phoneEmail.isEmpty()
                ? null
                : phoneEmail;
        this.password = password;
    }

    protected void loginUser(Context context, User user) throws Throwable
    {
        DatabaseHelper.getInstance(context).getUserDao().createOrUpdate(user);
        AppPrefs.getInstance(context).setCurrentUserId(user.id);
    }

    public static void logout(Context context)
    {
        AppPrefs.getInstance(context).logout();
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        if(phoneEmail == null)
        {
            return;
        }

        attemptRegistration(context);
    }

    protected abstract void attemptRegistration(Context context) throws Throwable;

    protected boolean matchesPhone(String phoneEmail)
    {
        Pattern p = Pattern.compile("\n");
        Matcher m = p.matcher(phoneEmail.substring(0, 1));
        return m.matches();
    }

}
