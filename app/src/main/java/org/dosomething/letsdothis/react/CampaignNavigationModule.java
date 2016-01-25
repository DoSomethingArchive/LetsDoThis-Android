package org.dosomething.letsdothis.react;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.dosomething.letsdothis.ui.CampaignDetailsActivity;

/**
 * A React Native Module to navigate to the CampaignDetailsActivity with a specified campaign ID.
 *
 * Created by juy on 1/13/16.
 */
public class CampaignNavigationModule extends ReactContextBaseJavaModule {
    ReactApplicationContext mContext;

    public CampaignNavigationModule(ReactApplicationContext reactContext) {
        super(reactContext);

        mContext = reactContext;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @ReactMethod
    public void presentCampaignWithCampaignID(int campaignId) {
        Intent i = CampaignDetailsActivity.getNewActivityLaunchIntent(mContext, campaignId);
        mContext.startActivity(i);
    }

}