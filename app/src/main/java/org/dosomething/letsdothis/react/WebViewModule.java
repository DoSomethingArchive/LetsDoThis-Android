package org.dosomething.letsdothis.react;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.dosomething.letsdothis.ui.WebViewActivity;

/**
 * A React Native module to open a URL in our WebViewActivity.
 *
 * Created by juy on 1/25/16.
 */
public class WebViewModule extends ReactContextBaseJavaModule {
    ReactApplicationContext mContext;

    public WebViewModule(ReactApplicationContext reactContext) {
        super(reactContext);

        mContext = reactContext;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @ReactMethod
    public void open(String url) {
        Intent i = WebViewActivity.getLaunchIntent(mContext, url);
        mContext.startActivity(i);
    }
}
