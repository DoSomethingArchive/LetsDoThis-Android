package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.apache.http.HttpStatus;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseLogin;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.android.threading.tasks.TaskQueue;
import retrofit.RetrofitError;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginTask extends BaseRegistrationTask
{

    public LoginTask(String email, String phone, String password)
    {
        super(email, phone, password);
    }

    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        ResponseLogin response = null;
        if(email != null)
        {
            response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .loginWithEmail(email, password);
        }
        else if(phone != null)
        {
            response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .loginWithMobile(phone, password);
        }

        if(response != null)
        {
            if(response._id != null)
            {
                User user = new User(email, phone, null);
                user.id = response._id;
                loginUser(context, user);

                TaskQueue.loadQueueDefault(context).execute(new GetUserTask(user.id));
            }
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        if(((RetrofitError) throwable).getResponse()
                .getStatus() == HttpStatus.SC_PRECONDITION_FAILED)
        {
            return true;
        }
        return super.handleError(context, throwable);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
