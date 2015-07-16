package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        invite1 = (EditText) findViewById(R.id.invite1);
        invite2 = (EditText) findViewById(R.id.invite2);
        invite3 = (EditText) findViewById(R.id.invite3);

        if(BuildConfig.DEBUG)
        {
            invite1.setText("Red");
            invite2.setText("Blunt");
            invite3.setText("Crane");
        }

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
            Integer groupId = 0;
            boolean allFilled = ! TextUtils.isEmpty(invite1.getText()) && ! TextUtils
                    .isEmpty(invite2.getText()) && ! TextUtils.isEmpty(invite3.getText());
            if(allFilled)
            {
                String code = InvitesAdapter.getCode(invite1, invite2, invite3);
                groupId = Hashery.getInstance(this).decode(code);
            }
            broadcastLogInSuccess(this);
            Toast.makeText(this, "success login", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getLaunchIntent(this, groupId, allFilled));
        }
        else
        {
            Toast.makeText(this, "failed login", Toast.LENGTH_SHORT).show();
        }
    }

}
