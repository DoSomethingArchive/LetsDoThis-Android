package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.network.models.ResponseUserUpdate;
import org.json.JSONObject;

import java.sql.SQLException;

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
        ResponseUserUpdate response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                .updateUser(user.id, jsonTypedInput);

        ResponseUser[] responseUsers = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                .userProfile(user.id);

        updatedUser = ResponseUser.getUser(responseUsers[0]);
        updatedUser = mergeDbUser(context, updatedUser);

        Dao<User, String> userDao = DatabaseHelper.getInstance(context).getUserDao();
        userDao.createOrUpdate(updatedUser);
    }

    public static User mergeDbUser(Context context, User updatedUser) throws SQLException
    {
        Dao<User, String> userDao = DatabaseHelper.getInstance(context).getUserDao();
        User dbUser = userDao.queryForId(updatedUser.id);
        //FIXME merge users
        return updatedUser;
    }


    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
