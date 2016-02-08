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

    // Flag indicating whether the user doc on the server needs to be updated after login
    public boolean mUserNeedsUpdate;

    // User object after logging in
    public User mLoggedInUser;

    public LoginTask(String login, String password)
    {
        super(login, password);

        mLogin = login;
        mUserNeedsUpdate = false;
        mLoggedInUser = null;
    }

    /**
     * @TODO this feels like a misnomer because only a login is being attempted here, not a registration
     *
     * @param context
     * @param country
     * @throws Throwable
     */
    @Override
    protected void attemptRegistration(Context context, String country) throws Throwable {
        ResponseLogin response;
        User user = new User();
        user.country = country;

        if (matchesEmail(mLogin)) {
            response = NetworkHelper.getNorthstarAPIService().loginWithEmail(mLogin, mPassword);
            user.email = mLogin;
        }
        else {
            response = NetworkHelper.getNorthstarAPIService().loginWithMobile(mLogin, mPassword);
            user.mobile = mLogin;
        }

        mLoggedInUser = validateResponse(context, response, user);
    }

    private User validateResponse(Context context, ResponseLogin response, User user) throws Throwable {
        if (response != null && response.data != null && response.data.key != null
            && response.data.user != null && response.data.user.data != null
            && response.data.user.data.id != null) {
            user.id = response.data.user.data.id;
            user.drupalId = response.data.user.data.drupal_id;
            user.email = response.data.user.data.email;
            user.mobile = response.data.user.data.mobile;
            user.first_name = response.data.user.data.first_name;
            user.last_name = response.data.user.data.last_name;
            user.birthdate = response.data.user.data.birthday;
            user.avatarPath = response.data.user.data.photo;

            AppPrefs appPrefs = AppPrefs.getInstance(context);
            appPrefs.setSessionToken(response.data.key);
            appPrefs.setCurrentEmail(response.data.user.data.email);
            loginUser(context, user);

            DatabaseHelper.getInstance(context).getUserDao().createOrUpdate(user);

            if (!user.country.equalsIgnoreCase(response.data.user.data.country)) {
                mUserNeedsUpdate = true;
            }
        }

        return user;
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
