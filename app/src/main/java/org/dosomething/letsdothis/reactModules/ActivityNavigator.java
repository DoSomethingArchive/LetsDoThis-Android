package org.dosomething.letsdothis.reactModules;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.dosomething.letsdothis.ui.CampaignDetailsActivity;

/**
 * Created by juy on 1/13/16.
 */
public class ActivityNavigator extends ReactContextBaseJavaModule {
    ReactApplicationContext mContext;

    public ActivityNavigator(ReactApplicationContext reactContext) {
        super(reactContext);

        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "ActivityNavigator";
    }

    @ReactMethod
    public void start(int campaignId) {
        Intent i = CampaignDetailsActivity.getNewActivityLaunchIntent(mContext, campaignId);
        mContext.startActivity(i);
    }

}
