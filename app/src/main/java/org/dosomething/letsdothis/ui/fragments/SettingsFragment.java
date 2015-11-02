package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.BaseActivity;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * Created by izzyoji :) on 4/29/15.
 */
public class SettingsFragment extends PreferenceFragment implements ConfirmDialog.ConfirmListener
{
    private SetTitleListener setTitleListener;

    // Google Analytics tracker
    private Tracker mTracker;

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
        initFeedback();
        initSuggestionLink();

        mTracker = ((LDTApplication)getActivity().getApplication()).getDefaultTracker();
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

                        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR,
                                AnalyticsUtils.ACTION_TAP_REVIEW_APP_BUTTON);

                        return true;
                    }
                });
    }

    private void initFeedback() {
        findPreference(getString(R.string.pref_feedback))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        final String url = "https://docs.google.com/a/dosomething.org/forms/d/1KUWQgfuoKpUXg7uuurXSgYQ3RCuxwNVSrGeb_kDRqf8/viewform";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);

                        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR,
                                AnalyticsUtils.ACTION_TAP_FEEDBACK_FORM);

                        return true;
                    }
                });
    }

    private void initSuggestionLink() {
        findPreference(getString(R.string.pref_suggestion_link))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.dosomething.org/campaigns/submit-your-idea"));
                        startActivity(intent);

                        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR,
                                AnalyticsUtils.ACTION_TAP_IDEAS_FORM);

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
