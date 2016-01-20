package org.dosomething.letsdothis.react;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.dosomething.letsdothis.BuildConfig;

/**
 * A React Native Module to pass config values from native to React Native.
 *
 * Created by juy on 1/19/16.
 */
public class ConfigModule extends ReactContextBaseJavaModule {

    public ConfigModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Sends an event with the News API URL to be used for the set build type.
     *
     * @param callback The JS method to invoke to return the URL back to JS
     */
    @ReactMethod
    public void getNewsUrl(Callback callback) {
        final String production = "https://live-ltd-news.pantheon.io/?json=1";
        final String internal = "https://live-ltd-news.pantheon.io/?json=1";
        final String dev = "https://dev-ltd-news.pantheon.io/?json=1";

        if (BuildConfig.BUILD_TYPE.equals("release")) {
            callback.invoke(production);
        }
        else if (BuildConfig.BUILD_TYPE.equals("internal")) {
            callback.invoke(internal);
        }
        else {
            callback.invoke(dev);
        }
    }
}
