package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.client.Response;

/**
 * Created by toidiu on 4/17/15.
 */
public class UpdateUserTask extends BaseNetworkErrorHandlerTask
{

    private final User user;

    public UpdateUserTask(User user)
    {
        this.user = user;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        Response response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                .updateUser(user.id, user);
        NetworkHelper.debugOut(response);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
