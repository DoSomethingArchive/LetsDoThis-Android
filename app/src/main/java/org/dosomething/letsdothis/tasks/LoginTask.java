package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseLogin;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.net.HttpURLConnection;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.RetrofitError;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginTask extends BaseRegistrationTask
{
    private final String mLogin;

    public LoginTask(String login, String password)
    {
        super(login, password);

        mLogin = login;
    }

    /**
     * @TODO this feels like a misnomer because only a login is being attempted here, not a registration
     *
     * @param context
     * @throws Throwable
     */
    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        ResponseLogin response;
        User user;

        if(matchesEmail(mLogin))
        {
            response = NetworkHelper.getNorthstarAPIService().loginWithEmail(mLogin, mPassword);
            user = new User(mLogin, null, null);
        }
        else
        {
            response = NetworkHelper.getNorthstarAPIService().loginWithMobile(mLogin, mPassword);
            user = new User(null, mLogin, null);
        }

        validateResponse(context, response, user);
    }

    private void validateResponse(Context context, ResponseLogin response, User user) throws Throwable
    {
        if(response != null)
        {
            if(response.data._id != null)
            {
                //FIXME should we get back a user avatar??
                user.id = response.data._id;
                user.drupalId = response.data.drupal_id;
                user.email = response.data.email;
                user.mobile = response.data.mobile;
                user.first_name = response.data.first_name;
                user.last_name = response.data.last_name;
                user.birthdate = response.data.birthday;
                user.avatarPath = response.data.avatar;
                AppPrefs.getInstance(context).setSessionToken(response.data.session_token);
                loginUser(context, user);

                DatabaseHelper.getInstance(context).getUserDao().createOrUpdate(user);
            }
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        if(throwable instanceof RetrofitError && ((RetrofitError) throwable).getResponse()
                .getStatus() == HttpURLConnection.HTTP_PRECON_FAILED)
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
