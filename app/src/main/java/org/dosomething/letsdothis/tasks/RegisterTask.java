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
    private final String firstName;
    private final String lastName;
    private final String birthday;

    public RegisterTask(String phoneEmail, String password, String firsttext, String lastText, String birthText)
    {
        super(phoneEmail, password);
        firstName = firsttext;
        lastName = lastText;
        birthday = birthText;
    }

    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        ResponseRegister response;
        User user = new User(password, firstName, lastName, birthday);

        if(matchesEmail(phoneEmail))
        {
            user.email = phoneEmail;
            response = NetworkHelper.getNorthstarAPIService().registerWithEmail(user);
            user = new User(phoneEmail, null, null);
        }
        else
        {
            user.mobile = phoneEmail;
            response = NetworkHelper.getNorthstarAPIService().registerWithMobile(user);

            user = new User(null, phoneEmail, null);
        }

        validateResponse(context, response, user);

    }

    private void validateResponse(Context context, ResponseRegister response, User user) throws Throwable
    {
        if(response != null)
        {
            if(response.data._id != null)
            {
                user.id = response.data._id;
                user.email = response.data.email;
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
