package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetUserTask extends BaseNetworkErrorHandlerTask {
    public User user;

    public GetUserTask() {
    }

    @Override
    protected void run(Context context) throws Throwable {
        AppPrefs appPrefs = AppPrefs.getInstance(context);
        String token = appPrefs.getSessionToken();
        ResponseUser response = NetworkHelper.getNorthstarAPIService().userProfile(token);

        user = ResponseUser.getUser(response);

        // @TODO we should maybe not allow this to run if id != user current id
        DatabaseHelper.getInstance(context).getUserDao().createOrUpdate(user);
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
