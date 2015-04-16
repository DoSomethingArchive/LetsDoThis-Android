package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.UserListResponse;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetUserTask extends BaseNetworkErrorHandlerTask
{
    public User user;

    @Override
    protected void run(Context context) throws Throwable
    {
        UserListResponse response = DataHelper.makeRequestAdapter().create(NorthstarAPI.class).userProfile();
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
