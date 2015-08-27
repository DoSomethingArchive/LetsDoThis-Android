package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LoginTask;
import org.dosomething.letsdothis.ui.adapters.InvitesAdapter;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.dosomething.letsdothis.utils.Hashery;

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
    private EditText invite1;
    private EditText invite2;
    private EditText invite3;


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

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
            }
        });

        if(BuildConfig.DEBUG)
        {
            phoneEmail.setText("touch@lab.co");
            password.setText("test");
        }
    }

    private void initLoginListener()
    {
        phoneEmail = (EditText) findViewById(R.id.phone_email);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String usertext = phoneEmail.getText().toString();
                String passtext = password.getText().toString();
                if(TextUtils.isEmpty(usertext))
                {
                    phoneEmail.setError("Must not be empty");
                }
                else if(TextUtils.isEmpty(passtext))
                {
                    password.setError("Must not be empty");
                }
                else
                {
                    TaskQueue.loadQueueDefault(LoginActivity.this)
                             .execute(new LoginTask(usertext, passtext));
                }

            }
        });

    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(LoginTask task)
    {
        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            broadcastLogInSuccess(this);
            startActivity(MainActivity.getLaunchIntent(this));
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.snack), R.string.fail_login, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.snack_error));
            snackbar.show();
        }
    }

}
