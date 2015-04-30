package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Profile;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LoginTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String FB_PROFILE = "FB_PROFILE";

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private EditText phoneEmail;
    private EditText password;


    public static Intent getLaunchIntent(Context context, Profile fbProfile)
    {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(FB_PROFILE, fbProfile);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBusExt.getDefault().register(this);

        //FIXME we have out fb profile info
        Profile profile = getIntent().getParcelableExtra(FB_PROFILE);
        profile.getFirstName();

        initLoginListener();

        if(BuildConfig.DEBUG)
        {
            phoneEmail.setText("touch@lab.co");
            password.setText("test");
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
            Toast.makeText(this, "success login", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this));
            finish();
        }
        else
        {
            Toast.makeText(this, "failed login", Toast.LENGTH_SHORT).show();
        }
    }

}
