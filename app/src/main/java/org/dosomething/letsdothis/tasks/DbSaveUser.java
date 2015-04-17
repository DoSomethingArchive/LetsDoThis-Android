package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by toidiu on 4/17/15.
 */
public class DbSaveUser extends Task
{
    private User user;

    public DbSaveUser(User user)
    {
        this.user = user;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        Dao<User, String> userDao = DatabaseHelper.getInstance(context).getUserDao();

        //FIXME do merging with existing user in the database
        userDao.createOrUpdate(user);
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
