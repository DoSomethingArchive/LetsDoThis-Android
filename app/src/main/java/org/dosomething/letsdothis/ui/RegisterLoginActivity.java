package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.FbUser;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterLoginActivity extends BaseActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String       TAG            = RegisterLoginActivity.class.getSimpleName();
    private static final List<String> FB_PERMISSIONS = Arrays
            .asList("public_profile", "email", "user_friends");


    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    public CallbackManager callbackManager;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, RegisterLoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                finish();
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startRegisterActivity(null);
            }
        });
    }

    private void startRegisterActivity(FbUser user)
    {
        startActivity(RegisterActivity.getLaunchIntent(RegisterLoginActivity.this, user));
        finish();
    }

    private void initFbConnect()
    {
        if(Profile.getCurrentProfile() != null)
        {
            LDTApplication.loginManager.logOut();
        }

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
                                final GraphRequest request = GraphRequest
                                        .newMeRequest(loginResult.getAccessToken(),
                                                      new GraphRequest.GraphJSONObjectCallback()
                                                      {
                                                          @Override
                                                          public void onCompleted(JSONObject object, GraphResponse response)
                                                          {
                                                              if(response.getError() == null)
                                                              {
                                                                  FbUser user = new Gson().fromJson(
                                                                          response.getRawResponse(),
                                                                          FbUser.class);
                                                                  startRegisterActivity(user);
                                                              }
                                                          }
                                                      });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields",
                                                     "id,email, gender, birthday, first_name, last_name");
                                request.setParameters(parameters);
                                request.executeAsync();
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
        if(callbackManager.onActivityResult(requestCode, resultCode, data))
        {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
