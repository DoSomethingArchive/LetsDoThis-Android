package org.dosomething.letsdothis.react;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * A React Native module for launching a Share intent.
 *
 * Created by juy on 2/1/16.
 */
public class ShareIntentModule extends ReactContextBaseJavaModule {

    ReactApplicationContext mContext;

    // Google Analytics tracker
    private Tracker mTracker;

    public ShareIntentModule(ReactApplicationContext reactContext, Tracker tracker) {
        super(reactContext);

        mContext = reactContext;
        mTracker = tracker;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @ReactMethod
    public void share(String articleName, String url, int postId) {
        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_NEWS,
                AnalyticsUtils.ACTION_SHARE_NEWS, String.valueOf(postId));

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        // Subject
        share.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name));

        // Text
        String defaultMessage = String.format(
                mContext.getString(R.string.share_news),
                articleName,
                url);
        share.putExtra(Intent.EXTRA_TEXT, defaultMessage);

        // Flag to allow activity to be launched from outside an Activity
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(share);
    }

}
