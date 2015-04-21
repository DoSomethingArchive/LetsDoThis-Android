package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseSignup;

import java.util.Date;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/15/15.
 */
public class SignupTask extends BaseRegistrationTask
{

    private final String firstName;
    private final String lastName;
    private final String   birthday;

    public SignupTask(String phoneEmail, String password, String firsttext, String lastText, String birthText)
    {
        super(phoneEmail, password);
        firstName = firsttext;
        lastName = lastText;
        birthday = birthText;
    }

    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        ResponseSignup response;
        User user = new User(password, firstName, lastName, birthday);


        if(matchesPhone(phoneEmail))
        {
            user.mobile = phoneEmail;
            response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .registerWithMobile(user);

            user = new User(null, phoneEmail, null);
            validateResponse(context, response, user);
        }
        else
        {
            user.email = phoneEmail;
            response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .registerWithEmail(user);
            user = new User(phoneEmail, null, null);
            validateResponse(context, response, user);
        }

    }

    private void validateResponse(Context context, ResponseSignup response, User user) throws Throwable
    {
        if(response != null)
        {
            if(response._id != null)
            {
                user.id = response._id;
                loginUser(context, user);
            }
        }
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
