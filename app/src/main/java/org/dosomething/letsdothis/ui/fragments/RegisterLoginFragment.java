package org.dosomething.letsdothis.ui.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.dosomething.letsdothis.ui.LoginActivity;
import org.dosomething.letsdothis.ui.RegisterActivity;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by toidiu on 4/21/15.
 */
public class RegisterLoginFragment extends Fragment
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final  String       TAG            = RegisterLoginFragment.class.getSimpleName();
    private static final List<String> FB_PERMISSIONS = Arrays
            .asList("public_profile", "email", "user_friends");

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    public CallbackManager callbackManager;

    public static RegisterLoginFragment newInstance()
    {
        Bundle bundle = new Bundle();
        RegisterLoginFragment fragment = new RegisterLoginFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppPrefs.getInstance(getActivity()).setFirstIntro(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        initAppNavigation(rootView);
        initFbConnect(rootView);

        return rootView;
    }

    private void initAppNavigation(final View rootView)
    {
        rootView.findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(LoginActivity.getLaunchIntent(getActivity()));
            }
        });
        rootView.findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
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
        startActivity(RegisterActivity.getLaunchIntent(getActivity(), user));
    }


    private void initFbConnect(View rootView)
    {
        if(Profile.getCurrentProfile() != null)
        {
            LDTApplication.loginManager.logOut();
        }

        callbackManager = CallbackManager.Factory.create();
        rootView.findViewById(R.id.fb_connect).setOnClickListener(new View.OnClickListener()

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
                                                     "id,email, birthday, first_name, last_name");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel()
                            {
                                if(BuildConfig.DEBUG)
                                {
                                    Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void onError(FacebookException e)
                            {
                                if(BuildConfig.DEBUG)
                                {
                                    Toast.makeText(getActivity(), e.getMessage(),
                                                   Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                LDTApplication.loginManager.logInWithReadPermissions(getActivity(), FB_PERMISSIONS);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(callbackManager.onActivityResult(requestCode, resultCode, data))
        {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
