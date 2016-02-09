package org.dosomething.letsdothis.react;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * A React Native Module to navigate to the CampaignDetailsActivity with a specified campaign ID.
 *
 * Created by juy on 1/13/16.
 */
public class CampaignNavigationModule extends ReactContextBaseJavaModule {
    ReactApplicationContext mContext;

    // Google Analytics tracker
    private Tracker mTracker;

    public CampaignNavigationModule(ReactApplicationContext reactContext, Tracker tracker) {
        super(reactContext);

        mContext = reactContext;
        mTracker = tracker;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @ReactMethod
    public void presentCampaignWithCampaignID(int campaignId, int postId) {
        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_NEWS,
                AnalyticsUtils.ACTION_TAKE_ACTION, String.valueOf(postId));

        Intent i = CampaignDetailsActivity.getNewActivityLaunchIntent(mContext, campaignId);
        mContext.startActivity(i);
    }

}