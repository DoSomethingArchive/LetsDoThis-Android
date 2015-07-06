package org.dosomething.letsdothis;
import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.parse.Parse;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class LDTApplication extends Application
{
    public static Context      context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
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
