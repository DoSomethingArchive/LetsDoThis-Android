package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.mime.TypedInput;

/**
 * Task to update a user's data on both the service and local storage.
 *
 * Created by toidiu on 4/17/15.
 */
public class UpdateUserTask extends BaseNetworkErrorHandlerTask {
    private final User user;
    public        User updatedUser;

    public UpdateUserTask(User user)
    {
        this.user = user;
    }

    @Override
    protected void run(Context context) throws Throwable {

        AppPrefs appPrefs = AppPrefs.getInstance(context);
        String token = appPrefs.getSessionToken();

        TypedInput jsonTypedInput = User.getJsonTypedInput(user);
        NorthstarAPI northstarAPIService = NetworkHelper.getNorthstarAPIService();

        // @todo Do two calls need to happen here? Could we just get by with the updateUser() and
        // its response?
        northstarAPIService.updateUser(token, jsonTypedInput);
        ResponseUser responseUsers = northstarAPIService.userProfile(token);

        updatedUser = ResponseUser.getUser(responseUsers);
        Dao<User, String> userDao = DatabaseHelper.getInstance(context).getUserDao();
        userDao.createOrUpdate(updatedUser);
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
