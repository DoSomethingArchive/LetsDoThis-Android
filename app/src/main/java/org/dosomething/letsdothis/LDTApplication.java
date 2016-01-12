package org.dosomething.letsdothis;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import io.fabric.sdk.android.Fabric;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class LDTApplication extends Application
{
    public static Context      context;

    // Google Analytics Tracker
    private Tracker mTracker;

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            // To enable debug logging, use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }

        return mTracker;
    }
}
