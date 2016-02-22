package org.dosomething.letsdothis.ui;
import android.os.Bundle;

import org.dosomething.letsdothis.utils.AppPrefs;

/**
 * Created by toidiu on 4/15/15.
 */
public class StartActivity extends BaseActivity {
    private static final String TAG = StartActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppPrefs.getInstance(this).isLoggedIn()) {
            goToMain();
        }
        else if (!AppPrefs.getInstance(this).isFirstIntro()) {
            goToRegisterLogin();
        }
        else {
            goToOnboarding();
        }
    }

    private void goToRegisterLogin() {
        startActivity(RegisterLoginActivity.getLaunchIntent(this));
        finish();
    }

    private void goToMain() {
        startActivity(MainActivity.getLaunchIntent(this));
        finish();
    }

    private void goToOnboarding() {
        startActivity(IntroActivity.getLaunchIntent(StartActivity.this));
        finish();
    }

}
