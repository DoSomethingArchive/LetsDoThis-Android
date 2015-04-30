package org.dosomething.letsdothis.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

/**
 * Created by kgalligan on 8/1/14.
 */
public class AppPrefs
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
    public static final  String FIRST_RUN = "FIRST_RUN";

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

    public boolean isLoggedIn()
    {
        return (prefs.getString(CURRENT_USER_ID, null) != null);
    }

    public void logout()
    {
        prefs.edit().putString(CURRENT_USER_ID, null).apply();
    }

    public void setCurrentUserId(String id)
    {
        prefs.edit().putString(CURRENT_USER_ID, id).apply();
    }

    public String getCurrentUserId()
    {
        return prefs.getString(CURRENT_USER_ID, null);
    }

    public void setFirstRun(boolean first)
    {
        prefs.edit().putBoolean(FIRST_RUN, first).apply();
    }

    public boolean isFirstRun()
    {
        return prefs.getBoolean(FIRST_RUN, true);
    }
}