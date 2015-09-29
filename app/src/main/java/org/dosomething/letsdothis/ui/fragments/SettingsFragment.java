package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.BaseActivity;

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
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
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

    private void initRate() {
        findPreference(getString(R.string.rate))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        final String packageName = "org.dosomething.letsdothis";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        try {
                            intent.setData(Uri.parse("market://details?id=" + packageName));
                            startActivity(intent);
                        }
                        catch (ActivityNotFoundException e) {
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                            startActivity(intent);
                        }

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
