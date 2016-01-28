package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.RegisterLoginFragment;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterLoginActivity extends BaseActivity {
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String TAG = RegisterLoginActivity.class.getSimpleName();

    // Google Analytics tracker
    private Tracker mTracker;

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, RegisterLoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lightning_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, RegisterLoginFragment.newInstance(),
                         RegisterLoginFragment.TAG).commit();
        }

        mTracker = ((LDTApplication)getApplication()).getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();

        AnalyticsUtils.sendScreen(mTracker, AnalyticsUtils.SCREEN_USER_CONNECT);
    }
}
