package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 4/29/15.
 */
public class NotificationSettingsFragment extends PreferenceFragment
{

    private SetTitleListener setTitleListener;

    public static NotificationSettingsFragment newInstance()
    {
        return new NotificationSettingsFragment();
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.addPreferencesFromResource(R.xml.preference_notification);

        initChangeNotifications();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        setTitleListener = (SetTitleListener) getActivity();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setTitleListener.setTitle(getResources().getString(R.string.notification_pref));
    }

    private void initChangeNotifications()
    {

        findPreference(getString(R.string.notification_kudos))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        //FIXME-----
                        Log.d("------", "----------ku");
                        return true;
                    }
                });
        findPreference(getString(R.string.notification_campaign))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        //FIXME-----
                        Log.d("------", "----------cam");
                        return true;
                    }
                });
        findPreference(getString(R.string.notification_friend))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        //FIXME-----
                        Log.d("------", "----------fri");
                        return true;
                    }
                });
    }


}
