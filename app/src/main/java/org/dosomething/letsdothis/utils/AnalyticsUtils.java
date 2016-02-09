package org.dosomething.letsdothis.utils;

import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by juy on 10/15/15.
 */
public class AnalyticsUtils {

    // Log tag
    private static final String TAG = "AnalyticsUtils";

    // Event category names
    public static final String CATEGORY_ACCOUNT = "account";
    public static final String CATEGORY_CAMPAIGN = "campaign";
    public static final String CATEGORY_BEHAVIOR = "behavior";
    public static final String CATEGORY_NEWS = "news";

    // Account category actions
    public static final String ACTION_FORGOT_PASSWORD = "forgot password";
    public static final String ACTION_TAP_AVATAR_BUTTON = "tap avatar button";
    public static final String ACTION_PROVIDE_MOBILE_NUMBER = "provide mobile number";

    // Campaign category actions
    public static final String ACTION_SUBMIT_SIGNUP = "submit signup";
    public static final String ACTION_SUBMIT_REPORTBACK = "submit reportback";

    // Behavior category actions
    public static final String ACTION_LOAD_MORE_PHOTOS = "load more photos";
    public static final String ACTION_EXPAND_CAMPAIGN_CELL = "expand campaign cell";
    public static final String ACTION_COLLAPSE_CAMPAIGN_CELL = "collapse campaign cell";
    public static final String ACTION_SHARE_PHOTO = "share photo";
    public static final String ACTION_LOG_OUT = "log out";
    public static final String ACTION_TAP_FEEDBACK_FORM = "tap on feedback form";
    public static final String ACTION_TAP_IDEAS_FORM = "tap on ideas form";
    public static final String ACTION_TAP_PRIVACY_POLICY = "tap on privacy policy";
    public static final String ACTION_TAP_REVIEW_APP_BUTTON = "tap on review app button";

    // News category actions
    public static final String ACTION_READ_NEWS = "read";
    public static final String ACTION_SHARE_NEWS = "share";
    public static final String ACTION_TAKE_ACTION = "take action";

    // Screen names
    public static final String SCREEN_CAMPAIGN = "campaign/%1$d/%2$s";
    public static final String SCREEN_CAUSE_LIST = "causes";
    public static final String SCREEN_CAUSE = "causes/%1$d";
    public static final String SCREEN_INTEREST_GROUP = "taxonomy_term/%1$d";
    public static final String SCREEN_NEWS = "news";
    public static final String SCREEN_ONBOARDING_1 = "onboarding-first";
    public static final String SCREEN_ONBOARDING_2 = "onboarding-second";
    public static final String SCREEN_REPORTBACK_FORM = "campaign/%1$d/reportbackform";
    public static final String SCREEN_REPORTBACK_ITEM = "reportback-item/%1$d";
    public static final String SCREEN_SETTINGS = "settings";
    public static final String SCREEN_USER_CONNECT = "user-connect";
    public static final String SCREEN_USER_LOGIN = "user-login";
    public static final String SCREEN_USER_PROFILE = "user-profile/%1$s";
    public static final String SCREEN_USER_REGISTER = "user-register";
    public static final String SCREEN_WEBVIEW = "webview";

    /**
     * Send event to Google Analytics.
     *
     * @param tracker Google Analytics Tracker
     * @param category The event category
     * @param action The event action
     * @param label The event label. Optional. If null or empty, it won't append a label to the event.
     */
    public static void sendEvent(Tracker tracker, String category, String action, String label) {
        if (category == null || action == null) {
            Log.e(TAG, "Failed to send a tracking event. category or action value is null");
            return;
        }

        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action);

        if (label != null && !label.isEmpty()) {
            eventBuilder.setLabel(label);
        }

        tracker.send(eventBuilder.build());
    }

    /**
     * Send event to Google Analytics.
     *
     * @param tracker Google Analytics Tracker
     * @param category The event category
     * @param action The event action
     */
    public static void sendEvent(Tracker tracker, String category, String action) {
        sendEvent(tracker, category, action, "");
    }

    /**
     * Send screen view to Google Analytics.
     *
     * @param tracker Google Analytics Tracker
     * @param screenName The screen name
     */
    public static void sendScreen(Tracker tracker, String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
