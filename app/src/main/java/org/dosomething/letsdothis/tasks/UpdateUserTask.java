package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.Helper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.RequestUserUpdate;

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
        String s = RequestUserUpdate.toJson(user);
        Response response = Helper.makeRequestAdapter().create(NorthstarAPI.class)
                .updateUser(user.id, s);
        Helper.debugOut(response);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
