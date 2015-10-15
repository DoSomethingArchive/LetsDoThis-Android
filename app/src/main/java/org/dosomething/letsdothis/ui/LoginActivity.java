package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LoginTask;
import org.dosomething.letsdothis.tasks.UpdateUserTask;
import org.dosomething.letsdothis.utils.AnalyticsUtils;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginActivity extends BaseActivity
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private EditText phoneEmail;
    private EditText password;

    // Google Analytics tracker
    private Tracker mTracker;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initLoginListener();
        initLightning();

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.getLaunchIntent(LoginActivity.this, null));
                finish();
            }
        });


        findViewById(R.id.forgot_pw).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                                  Uri.parse(getString(R.string.forgot_pw_link)));
                startActivity(browserIntent);

                AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_ACCOUNT,
                        AnalyticsUtils.ACTION_FORGOT_PASSWORD);
            }
        });

        mTracker = ((LDTApplication)getApplication()).getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();

        AnalyticsUtils.sendScreen(mTracker, AnalyticsUtils.SCREEN_USER_LOGIN);
    }

    private void initLoginListener()
    {
        phoneEmail = (EditText) findViewById(R.id.phone_email);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneEmail.setBackgroundResource(R.drawable.bg_white_rounded_rect_filled);
                password.setBackgroundResource(R.drawable.bg_white_rounded_rect_filled);

                boolean isValid = true;
                String usertext = phoneEmail.getText().toString();
                String passtext = password.getText().toString();
                if (TextUtils.isEmpty(usertext)) {
                    String error = getResources().getString(R.string.error_login_phone_email);
                    phoneEmail.setError(error);
                    phoneEmail.setBackgroundResource(R.drawable.edittext_error_background);
                    isValid = false;
                }

                if (TextUtils.isEmpty(passtext)) {
                    String error = getResources().getString(R.string.error_login_password);
                    password.setError(error);
                    password.setBackgroundResource(R.drawable.edittext_error_background);
                    isValid = false;
                }

                if (isValid) {
                    TaskQueue.loadQueueDefault(LoginActivity.this)
                             .execute(new LoginTask(usertext, passtext));
                }
            }
        });

    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(LoginTask task) {
        if (AppPrefs.getInstance(this).isLoggedIn()) {
            broadcastLogInSuccess(this);
            startActivity(MainActivity.getLaunchIntent(this));

            if (task.mUserNeedsUpdate) {
                TaskQueue.loadQueueDefault(LoginActivity.this).execute(new UpdateUserTask(task.mLoggedInUser));
            }
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.snack), R.string.fail_login, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.snack_error));
            snackbar.show();
        }
    }

}
