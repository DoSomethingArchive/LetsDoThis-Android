package org.dosomething.letsdothis;
import android.app.Application;
import android.content.Context;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class LDTApplication extends Application
{
    public static Context      context;
    public static LoginManager loginManager;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        loginManager = LoginManager.getInstance();
    }


    public static Context getContext()
    {
        if(context == null)
        {
            throw new RuntimeException("context is null");
        }
        return context;
    }
}
