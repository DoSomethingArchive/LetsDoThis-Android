package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LoginTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginActivity extends ActionBarActivity
{
    private static final String TAG = LoginActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private EditText phone;
    private EditText email;
    private EditText password;


    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBusExt.getDefault().register(this);

        initLoginListener();

        if(BuildConfig.DEBUG)
        {
            email.setText("touchlab-dev@example.com");
            password.setText("touchlab");
        }
    }

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initLoginListener()
    {
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String phonetext = phone.getText().toString();
                String emailtext = email.getText().toString();
                String passtext = password.getText().toString();
                TaskQueue.loadQueueDefault(LoginActivity.this)
                        .execute(new LoginTask(emailtext, phonetext, passtext));
            }
        });
    }



    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(LoginTask task)
    {
        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            Toast.makeText(this, "success login", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this));
        }
        else
        {
            Toast.makeText(this, "failed login", Toast.LENGTH_SHORT).show();
        }
    }

}
