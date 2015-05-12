package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.RegisterLoginFragment;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterLoginActivity extends AppCompatActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final String TAG = RegisterLoginActivity.class.getSimpleName();


    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, RegisterLoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lightening_container);


        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, RegisterLoginFragment.newInstance(),
                         RegisterLoginFragment.TAG).commit();
        }
    }

}
