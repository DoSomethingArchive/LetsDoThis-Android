package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by toidiu on 4/17/15.
 */
public class DbGetUser extends Task
{

    public User user;

    @Override
    protected void run(Context context) throws Throwable
    {
        String id = "54fa272b469c64d8068b4567"; //FIXME this is for testing
        user = new User();
        user.id = id;
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
