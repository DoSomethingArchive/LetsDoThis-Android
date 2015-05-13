package org.dosomething.letsdothis.ui.fragments;
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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.addPreferencesFromResource(R.xml.preference_general);

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

        findPreference(getString(R.string.logout)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                ConfirmDialog confirmDialog = ConfirmDialog.newInstance(getString(R.string.prompt_logout));
                confirmDialog.setListener(SettingsFragment.this);
                confirmDialog.show(getFragmentManager(), ConfirmDialog.TAG);
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
