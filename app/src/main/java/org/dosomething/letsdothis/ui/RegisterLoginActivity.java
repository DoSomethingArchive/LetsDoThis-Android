package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterLoginActivity extends AppCompatActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String       TAG            = RegisterLoginActivity.class.getSimpleName();
    private static final List<String> FB_PERMISSIONS = Arrays.asList("public_profile", "email");

    //~=~=~=~=~=~=~=~=~=~=~=~=Views

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, RegisterLoginActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initAppNavigation();
    }

    private void initAppNavigation()
    {
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(LoginActivity.getLaunchIntent(RegisterLoginActivity.this));
                finish();
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(SignupActivity.getLaunchIntent(RegisterLoginActivity.this));
                finish();
            }
        });

        findViewById(R.id.fb_connect).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LoginManager loginManager = LoginManager.getInstance();
                loginManager.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        if(BuildConfig.DEBUG)
                        {
                            Toast.makeText(getApplicationContext(),
                                           loginResult.getAccessToken().toString(),
                                           Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel()
                    {

                    }

                    @Override
                    public void onError(FacebookException e)
                    {
                        if(BuildConfig.DEBUG)
                        {
                            Toast.makeText(getApplicationContext(), e.getMessage(),
                                           Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                loginManager.logInWithReadPermissions(RegisterLoginActivity.this, FB_PERMISSIONS);
            }
        });
    }

}
