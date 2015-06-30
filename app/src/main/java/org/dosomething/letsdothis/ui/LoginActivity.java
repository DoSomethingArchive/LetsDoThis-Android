package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LoginTask;
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
                TaskQueue.loadQueueDefault(LoginActivity.this)
                        .execute(new LoginTask(usertext, passtext));
            }
        });

    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(LoginTask task)
    {
        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            broadcastLogInSuccess(this);
            Toast.makeText(this, "success login", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this));
        }
        else
        {
            Toast.makeText(this, "failed login", Toast.LENGTH_SHORT).show();
        }
    }

}
