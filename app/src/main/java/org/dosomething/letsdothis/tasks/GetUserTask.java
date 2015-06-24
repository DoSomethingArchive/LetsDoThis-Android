package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseUser;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetUserTask extends BaseNetworkErrorHandlerTask
{
    private final String id;
    public        User   user;

    public GetUserTask(String id)
    {
        this.id = id;
    }

    @Override
    protected void run(Context context) throws Throwable
    {

        ResponseUser response = NetworkHelper.getNorthstarAPIService().userProfile(id);
        user = ResponseUser.getUser(response);

        DatabaseHelper.getInstance(context).getUserDao().createOrUpdate(user);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
