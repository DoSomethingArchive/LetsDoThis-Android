package org.dosomething.letsdothis;
import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import io.fabric.sdk.android.Fabric;

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

        Fabric.with(this, new Crashlytics());
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
