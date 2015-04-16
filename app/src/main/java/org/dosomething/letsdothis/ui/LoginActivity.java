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

    public static void callMe(Context context)
    {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBusExt.getDefault().register(this);

        initLoginListener();
        initSignupListener();
        if(BuildConfig.DEBUG)
        {
            email.setText("touchlab-dev@example.com");
            password.setText("touchlab");
        }
    }

    private void initSignupListener()
    {
        View signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
                SignupActivity.callMe(LoginActivity.this);
            }
        });
    }

    private void initLoginListener()
    {
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        View login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener()
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
        if(task.success)
        {
            Toast.makeText(this, "success login", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this));
            finish();
        }
        else
        {
            Toast.makeText(this, "failed login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBusExt.getDefault().unregister(this);
    }
}
