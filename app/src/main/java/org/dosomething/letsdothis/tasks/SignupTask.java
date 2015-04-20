package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.models.SignupResponse;

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
        SignupResponse response = null;
        if(email != null)
        {
            User user = new User(email, phone, password);
            response = DataHelper.getNorthstarAPIService()
                    .registerWithEmail(User.getJso(user));
        }
        else if(phone != null)
        {
            String regInfo = "{mobile: " + phone + ", password: " + password + "}";
            response = DataHelper.getNorthstarAPIService()
                    .registerWithMobile(regInfo);
        }

        if(response != null)
        {
            if(response._id != null)
            {
                success = true;
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
