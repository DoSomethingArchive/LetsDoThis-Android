package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.UserResponse;

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

        UserResponse[] response = DataHelper.makeRequestAdapter().create(NorthstarAPI.class)
                .userProfile(id);
        user = UserResponse.getUser(response[0]);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
