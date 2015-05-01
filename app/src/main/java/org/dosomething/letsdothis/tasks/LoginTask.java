package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.apache.http.HttpStatus;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseLogin;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import retrofit.RetrofitError;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginTask extends BaseRegistrationTask
{

    public LoginTask(String phoneEmail, String password)
    {
        super(phoneEmail, password);
    }

    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        ResponseLogin response;
        User user;

        if(matchesEmail(phoneEmail))
        {
            response = NetworkHelper.getNorthstarAPIService().loginWithEmail(phoneEmail, password);

            user = new User(phoneEmail, null, null);
        }
        else
        {
            response = NetworkHelper.getNorthstarAPIService().loginWithMobile(phoneEmail, password);

            user = new User(null, phoneEmail, null);
        }

        validateResponse(context, response, user);
    }

    private void validateResponse(Context context, ResponseLogin response, User user) throws Throwable
    {
        if(response != null)
        {
            if(response._id != null)
            {
                user.id = response._id;
                AppPrefs.getInstance(context).setSessionToken(response.session_token);
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
