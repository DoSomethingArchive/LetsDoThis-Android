package org.dosomething.letsdothis.ui;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LogoutTask;
import org.dosomething.letsdothis.tasks.UploadAvatarTask;
import org.dosomething.letsdothis.ui.fragments.SetTitleListener;
import org.dosomething.letsdothis.ui.fragments.SettingsFragment;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * Activity to display and change the app settings.
 *
 * Created by izzyoji :) on 4/29/15.
 */
public class SettingsActivity extends BaseActivity implements SetTitleListener,
        SettingsFragment.ShowProgressDialogListener
{

    private CustomToolbar toolbar;

    // Google Analytics tracker
    private Tracker mTracker;

    // Progress Dialog
    private ProgressDialog mProgressDialog;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_settings);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance()).commit();

        mTracker = ((LDTApplication)getApplication()).getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();

        AnalyticsUtils.sendScreen(mTracker, AnalyticsUtils.SCREEN_SETTINGS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(LogoutTask task) {
        dismissProgressDialog();

        sendBroadcast(new Intent(BaseActivity.LOGOUT_SUCCESS));
        startActivity(RegisterLoginActivity.getLaunchIntent(this));

        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR, AnalyticsUtils.ACTION_LOG_OUT);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UploadAvatarTask task) {
        int stringResId = R.string.change_photo_confirmation;
        int colorResId = R.color.cerulean_1;

        if (task.hasError()) {
            stringResId = R.string.error_avatar_upload;
            colorResId = R.color.snack_error;
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.snack), stringResId, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(colorResId));
        snackbar.show();
    }

    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void setTitle(String title)
    {
        toolbar.setTitle(title);
    }

    @Override
    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(SettingsActivity.this);
        }

        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * Dismisses the ProgressDialog, if any.
     */
    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
