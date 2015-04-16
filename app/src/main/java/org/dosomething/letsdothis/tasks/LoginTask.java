package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.client.Response;

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
    protected void run(Context context) throws Throwable
    {
        Response response = null;
        if(email != null)
        {
            response = DataHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .loginWithEmail(email, password);
            DataHelper.debugOut(response);
        }
        else if(phone != null)
        {
            response = DataHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .loginWithMobile(phone, password);
        }

        if(response != null)
        {
            //FIXME this is not solid and will be handled when we have response objects
            success = true;
        }
    }


    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
