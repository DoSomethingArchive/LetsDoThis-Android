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
    private final String  id;
    private final boolean isDrupalId;
    public        User    user;

    public GetUserTask(String id, boolean isDrupalId)
    {
        this.id = id;
        this.isDrupalId = isDrupalId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {

        ResponseUser response;
        if(isDrupalId)
        {
            response = NetworkHelper.getNorthstarAPIService()
                    .userProfileWithDrupalId(id);
        }
        else
        {
            response = NetworkHelper.getNorthstarAPIService().userProfile(id);
        }
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
