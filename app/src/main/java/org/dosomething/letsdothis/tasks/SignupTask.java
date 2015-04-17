package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseSignup;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/15/15.
 */
public class SignupTask extends BaseRegistrationTask
{

    public SignupTask(String email, String phone, String password)
    {
        super(email, phone, password);
    }

    @Override
    protected void attemptRegistration(Context context) throws Throwable
    {
        ResponseSignup response = null;
        if(email != null)
        {
            User user = new User(email, phone, password);
            response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .registerWithEmail(User.getJson(user));
        }
        else if(phone != null)
        {
            String regInfo = "{mobile: " + phone + ", password: " + password + "}";
            response = NetworkHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .registerWithMobile(regInfo);
        }

        if(response != null)
        {
            if(response._id != null)
            {
                User user = new User(email, phone, null);
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
