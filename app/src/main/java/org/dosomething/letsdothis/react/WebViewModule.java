package org.dosomething.letsdothis.react;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.ui.WebViewActivity;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * A React Native module to open a URL in our WebViewActivity.
 *
 * Created by juy on 1/25/16.
 */
public class WebViewModule extends ReactContextBaseJavaModule {
    ReactApplicationContext mContext;

    // Google Analytics tracker
    private Tracker mTracker;

    public WebViewModule(ReactApplicationContext reactContext, Tracker tracker) {
        super(reactContext);

        mContext = reactContext;
        mTracker = tracker;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @ReactMethod
    public void open(String url, int postId) {
        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_NEWS,
                AnalyticsUtils.ACTION_READ_NEWS, String.valueOf(postId));

        Intent i = WebViewActivity.getLaunchIntent(mContext, url);
        mContext.startActivity(i);
    }
}
