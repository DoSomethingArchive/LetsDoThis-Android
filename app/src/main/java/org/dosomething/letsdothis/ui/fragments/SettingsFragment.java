package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.BaseActivity;
import org.dosomething.letsdothis.utils.AnalyticsUtils;
import org.dosomething.letsdothis.utils.AppPrefs;

/**
 * This fragment defines the behavior of the items on the settings screen.
 *
 * Created by izzyoji :) on 4/29/15.
 */
public class SettingsFragment extends PreferenceFragment implements ConfirmDialog.ConfirmListener
{
    // Listener for setting the screen title
    private SetTitleListener setTitleListener;

    // Listener for showing or dismissing a ProgressDialog
    private ShowProgressDialogListener mShowProgressDialogListener;

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
        initAccount();
        initFeedback();
        initSuggestionLink();
        displayVersionName();

        mTracker = ((LDTApplication)getActivity().getApplication()).getDefaultTracker();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        setTitleListener = (SetTitleListener) getActivity();
        mShowProgressDialogListener = (ShowProgressDialogListener) getActivity();
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

    private void initAccount() {
        String email = AppPrefs.getInstance(getActivity()).getCurrentEmail();
        if (email != null) {
            findPreference(getString(R.string.pref_email_key)).setTitle(email);
        }

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
                        intent.setData(Uri.parse("https://www.dosomething.org/us/about/submit-your-campaign-idea"));
                        startActivity(intent);

                        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR,
                                AnalyticsUtils.ACTION_TAP_IDEAS_FORM);

                        return true;
                    }
                });
    }

    private void displayVersionName() {
        String packageName = getActivity().getPackageName();
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(packageName, 0);
            String version = getString(R.string.pref_version_text, packageInfo.versionName);

            findPreference(getString(R.string.pref_version_key)).setTitle(version);
        }
        catch (PackageManager.NameNotFoundException e) {
            ; // Just don't display anything I guess? Or find some way to log it so we can see it.
        }
    }

    @Override
    public void onConfirmClick() {
        mShowProgressDialogListener.showProgressDialog(getString(R.string.progress_dialog_logout));

        BaseActivity.logOutUser(getActivity());
    }

    /**
     * Interface the fragment can communicate through to show and dismiss a ProgressDialog.
     */
    public interface ShowProgressDialogListener {
        /**
         * Create and display a ProgressDialog.
         *
         * @param message Message to display
         */
        void showProgressDialog(String message);
    }
}
