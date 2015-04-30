package org.dosomething.letsdothis.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

/**
 * Created by kgalligan on 8/1/14.
 */
public class AppPrefs
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    public static final String USER_SESSION_TOKEN = "CURRENT_SESSION_TOKEN";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private static AppPrefs          instance;
    private        SharedPreferences prefs;

    @NotNull
    public static synchronized AppPrefs getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new AppPrefs();
            instance.prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        }

        return instance;
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
        return !TextUtils.isEmpty(getString(CURRENT_USER_ID, null));
    }

    public void logout()
    {
        setCurrentUserId(null);
        setSessionToken(null);
    }

    public void setCurrentUserId(String id)
    {
        setString(CURRENT_USER_ID, id);
    }

    public String getCurrentUserId()
    {
        return getString(CURRENT_USER_ID, null);
    }

    public void setSessionToken(String token)
    {
        setString(USER_SESSION_TOKEN, token);
    }

    public String getSessionToken()
    {
        return getString(USER_SESSION_TOKEN, null);
    }
}