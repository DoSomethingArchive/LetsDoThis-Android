package org.dosomething.letsdothis.ui.fragments;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.BaseActivity;
import org.dosomething.letsdothis.ui.ChangeEmailActivity;
import org.dosomething.letsdothis.ui.ChangeNumberActivity;
import org.dosomething.letsdothis.ui.ChangePasswordActivity;

/**
 * Created by izzyoji :) on 4/29/15.
 */
public class SettingsFragment extends PreferenceFragment implements ConfirmDialog.ConfirmListener
{

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
        initChangeEmail();
        initChangeNotifications();
        initChangePassword();
        initChangePhone();
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

    private void initChangePhone()
    {
        findPreference(getString(R.string.change_number))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        startActivity(ChangeNumberActivity.getLaunchIntent(getActivity()));
                        return true;
                    }
                });
    }

    private void initChangeEmail()
    {
        findPreference(getString(R.string.change_email))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        startActivity(ChangeEmailActivity.getLaunchIntent(getActivity()));
                        return true;
                    }
                });
    }

    private void initChangePassword()
    {
        findPreference(getString(R.string.change_password))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
                {
                    @Override
                    public boolean onPreferenceClick(Preference preference)
                    {
                        startActivity(ChangePasswordActivity.getLaunchIntent(getActivity()));
                        return true;
                    }
                });
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
