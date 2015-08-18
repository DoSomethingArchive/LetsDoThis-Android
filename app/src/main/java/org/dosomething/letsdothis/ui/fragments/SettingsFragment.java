package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.MediaStore;
import android.util.Log;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.UploadAvatarTask;
import org.dosomething.letsdothis.ui.BaseActivity;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.io.File;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/29/15.
 */
public class SettingsFragment extends PreferenceFragment implements ConfirmDialog.ConfirmListener
{
    private SetTitleListener setTitleListener;

    public static SettingsFragment newInstance()
    {
        return new SettingsFragment();
    }


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.addPreferencesFromResource(R.xml.preference_general);

        initRate();
        initLogout();
        initChangeNotifications();
        initNotificationsPrefs();
        initChangePhoto();
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
        setTitleListener.setTitle(getResources().getString(R.string.settings));
    }

    private void initLogout()
    {
        findPreference(getString(R.string.logout))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        ConfirmDialog confirmDialog = ConfirmDialog
                                .newInstance(getString(R.string.prompt_logout));
                        confirmDialog.setListener(SettingsFragment.this);
                        confirmDialog.show(getFragmentManager(), ConfirmDialog.TAG);
                        return true;
                    }
                });
    }

    private void initChangePhoto()
    {
        findPreference(getString(R.string.change_photo))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new AvatarChangeFragment())
                                .addToBackStack(null).commit();
                        return true;
                    }
                });
    }

    private void initNotificationsPrefs()
    {
        Preference receiveNotifs = findPreference(getString(R.string.receive_notifications));
        updateChangeNotifsPref((SwitchPreference) receiveNotifs);
        receiveNotifs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {

                updateChangeNotifsPref((SwitchPreference) preference);
                return true;
            }
        });

        findPreference(getString(R.string.change_notifications_prefs))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotificationSettingsFragment())
                                .addToBackStack(null).commit();
                        return true;
                    }
                });
    }


    private void updateChangeNotifsPref(SwitchPreference preference)
    {
        boolean checked = preference.isChecked();
        findPreference(getString(R.string.change_notifications_prefs)).setEnabled(checked);
    }


    private void initChangeNotifications()
    {
        findPreference(getString(R.string.change_notifications_prefs))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, new NotificationSettingsFragment())
                                .addToBackStack(null).commit();
                        return true;
                    }
                });
    }

    private void initRate()
    {
        findPreference(getString(R.string.rate))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=org.dosomething.letsdothis"));
                        startActivity(intent);
                        return true;
                    }
                });
    }

    @Override
    public void onConfirmClick()
    {
        BaseActivity.logOutUser(getActivity());
    }
}
