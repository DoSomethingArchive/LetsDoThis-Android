package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseUser;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.mime.TypedInput;

/**
 * Created by toidiu on 4/17/15.
 */
public class UpdateUserTask extends BaseNetworkErrorHandlerTask
{

    private final User user;
    public        User updatedUser;

    public UpdateUserTask(User user)
    {
        this.user = user;
    }

    @Override
    protected void run(Context context) throws Throwable
    {

        TypedInput jsonTypedInput = User.getJsonTypedInput(user);
        NorthstarAPI northstarAPIService = NetworkHelper.getNorthstarAPIService();
//        northstarAPIService.updateUser(user.id, jsonTypedInput);

//        ResponseUser[] responseUsers = northstarAPIService
//                .userProfile(user.id);

//        updatedUser = ResponseUser.getUser(responseUsers[0]);
//        Dao<User, String> userDao = DatabaseHelper.getInstance(context).getUserDao();
//        userDao.createOrUpdate(updatedUser);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
