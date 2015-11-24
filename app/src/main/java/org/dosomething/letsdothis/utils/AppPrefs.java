package org.dosomething.letsdothis.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.dosomething.letsdothis.R;
import org.jetbrains.annotations.NotNull;

public class AppPrefs
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String CURRENT_EMAIL      = "CURRENT_EMAIL";
    public static final String CURRENT_USER_ID    = "CURRENT_USER_ID";
    public static final String CURRENT_DRUPAL_ID  = "CURRENT_DRUPAL_ID";
    public static final String USER_SESSION_TOKEN = "CURRENT_SESSION_TOKEN";
    public static final String FIRST_INTRO        = "FIRST_INTRO";
    public static final String FIRST_DRAWER       = "FIRST_DRAWER";
    public static final String AVATAR_PATH        = "AVATAR_PATH";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private static   AppPrefs          instance;
    volatile private Context           context;
    private          SharedPreferences prefs;

    @NotNull
    public static synchronized AppPrefs getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new AppPrefs();
            instance.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        instance.context = context;
        return instance;
    }

    private boolean getReceiveNotification()
    {
        boolean notification = prefs
                .getBoolean(this.context.getResources().getString(R.string.receive_notifications),
                            false);
        return notification;
    }

    private void setBoolean(String key, Boolean value)
    {
        prefs.edit().putBoolean(key, value).apply();
    }

    private Boolean getBoolean(String key, Boolean defaultVal)
    {
        return prefs.getBoolean(key, defaultVal);
    }

    private void setString(String key, String value)
    {
        prefs.edit().putString(key, value).apply();
    }

    private String getString(String key, String defaultVal)
    {
        return prefs.getString(key, defaultVal);
    }

    private void setInt(String key, Integer value)
    {
        prefs.edit().putInt(key, value).apply();
    }

    private Integer getInt(String key, Integer defaultVal)
    {
        return prefs.getInt(key, defaultVal);
    }

    private void setLong(String key, Long value)
    {
        prefs.edit().putLong(key, value).apply();
    }

    private Long getLong(String key, Long defaultVal)
    {
        return prefs.getLong(key, defaultVal);
    }

    public boolean isLoggedIn()
    {
        return ! TextUtils.isEmpty(getString(CURRENT_USER_ID, null));
    }

    public void logout() {
        setAvatarPath(null);
        setCurrentEmail(null);
        setCurrentDrupalId(-1);
        setCurrentUserId(null);
        setSessionToken(null);
    }

    public void setCurrentEmail(String email) {
        setString(CURRENT_EMAIL, email);
    }

    public String getCurrentEmail() {
        return getString(CURRENT_EMAIL, null);
    }

    public void setCurrentUserId(String id)
    {
        setString(CURRENT_USER_ID, id);
    }

    public String getCurrentUserId()
    {
        return getString(CURRENT_USER_ID, null);
    }

    public void setCurrentDrupalId(int drupalId)
    {
        setInt(CURRENT_DRUPAL_ID, drupalId);
    }

    public int getCurrentDrupalId()
    {
        return getInt(CURRENT_DRUPAL_ID, -1);
    }

    public void setSessionToken(String token)
    {
        setString(USER_SESSION_TOKEN, token);
    }

    public String getSessionToken()
    {
        return getString(USER_SESSION_TOKEN, null);
    }

    public void setFirstIntro(boolean first)
    {
        setBoolean(FIRST_INTRO, first);
    }

    public boolean isFirstIntro()
    {
        return getBoolean(FIRST_INTRO, true);
    }

    public void setFirstDrawer()
    {
        setBoolean(FIRST_DRAWER, false);
    }

    public boolean isFirstDrawer()
    {
        return getBoolean(FIRST_DRAWER, true);
    }

    public void setAvatarPath(String path)
    {
        setString(AVATAR_PATH, path);
    }

    public String getAvatarPath()
    {
        return getString(AVATAR_PATH, null);
    }
}