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
import com.facebook.Profile;
import com.facebook.login.LoginResult;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.Arrays;
import java.util.List;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterLoginActivity extends AppCompatActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String       TAG            = RegisterLoginActivity.class.getSimpleName();
    private static final List<String> FB_PERMISSIONS = Arrays
            .asList("public_profile", "email", "user_friends");


    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    public CallbackManager callbackManager;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, RegisterLoginActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppPrefs.getInstance(this).setFirstRun(false);

        initFbConnect();
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
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startSignupActivity(null);
            }
        });
    }

    private void startSignupActivity(Profile currentProfile)
    {
        startActivity(SignupActivity.getLaunchIntent(RegisterLoginActivity.this, currentProfile));
    }

    private void initFbConnect()
    {
        callbackManager = CallbackManager.Factory.create();
        findViewById(R.id.fb_connect).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LDTApplication.loginManager
                        .registerCallback(callbackManager, new FacebookCallback<LoginResult>()
                        {
                            @Override
                            public void onSuccess(LoginResult loginResult)
                            {
                                if(BuildConfig.DEBUG)
                                {
                                    Toast.makeText(getApplicationContext(),
                                                   loginResult.getAccessToken().toString(),
                                                   Toast.LENGTH_SHORT).show();
                                    Profile currentProfile = Profile.getCurrentProfile();
                                    startSignupActivity(currentProfile);
                                }
                            }

                            @Override
                            public void onCancel()
                            {
                                if(BuildConfig.DEBUG)
                                {
                                    Toast.makeText(getApplicationContext(), "Cancel",
                                                   Toast.LENGTH_SHORT).show();
                                }
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

                LDTApplication.loginManager
                        .logInWithReadPermissions(RegisterLoginActivity.this, FB_PERMISSIONS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
