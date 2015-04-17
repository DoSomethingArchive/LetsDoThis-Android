package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.SignupTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class SignupActivity extends ActionBarActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private EditText phone;
    private EditText email;
    private EditText password;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, SignupActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EventBusExt.getDefault().register(this);

        initRegisterListener();
    }

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initRegisterListener()
    {
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        View register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String phonetext = phone.getText().toString();
                String emailtext = email.getText().toString();
                String passtext = password.getText().toString();
                TaskQueue.loadQueueDefault(SignupActivity.this)
                        .execute(new SignupTask(emailtext, phonetext, passtext));
            }
        });
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(SignupTask task)
    {
        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            Toast.makeText(this, "success register", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "failed register", Toast.LENGTH_SHORT).show();
        }
    }

}
