package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseRegister;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterTask extends BaseRegistrationTask
{
    private final String SOURCE = "android";

    private final String mEmail;
    private final String mPhone;
    private final String mFirstName;

    public RegisterTask(String email, String phone, String password, String firstName)
    {
        super(email, password);

        mEmail = email;
        mPhone = phone;
        mFirstName = firstName;
    }

    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        User user = new User(mEmail, mPhone, mPassword);
        user.first_name = mFirstName;
        user.source = SOURCE;

        ResponseRegister response = NetworkHelper.getNorthstarAPIService().registerWithEmail(user);
        validateResponse(context, response, user);
    }

    private void validateResponse(Context context, ResponseRegister response, User user) throws Throwable
    {
        if(response != null)
        {
            if(response.data._id != null)
            {
                user.id = response.data._id;
                user.drupalId = response.data.drupal_id;
                user.email = response.data.email;
                user.mobile = response.data.mobile;
                user.birthdate = response.data.birthday;
                user.first_name = response.data.first_name;
                user.last_name = response.data.last_name;

                AppPrefs.getInstance(context).setSessionToken(response.data.session_token);
                loginUser(context, user);
                //FIXME: need to get the session token here
            }
        }
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }

}
