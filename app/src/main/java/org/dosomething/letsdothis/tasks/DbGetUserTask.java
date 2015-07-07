package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by toidiu on 4/17/15.
 */
public class DbGetUserTask extends Task
{
    private final String id;
    public        User   user;

    public DbGetUserTask(String id)
    {
        this.id = id;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        user = DatabaseHelper.getInstance(context).getUserDao().queryForId(id);
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
