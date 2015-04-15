package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;

import co.touchlab.android.threading.tasks.Task;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginTask extends Task
{

    private final String email;
    private final String phone;
    private final String password;

    public LoginTask(String email, String phone, String password)
    {
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        DataHelper.makeRequestAdapter().create(NorthstarAPI.class).loginWithEmail()
        DataHelper.makeRequestAdapter().create(NorthstarAPI.class).loginWithMobile()
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }
}
