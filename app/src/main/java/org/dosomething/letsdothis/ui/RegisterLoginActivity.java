package org.dosomething.letsdothis.ui;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.BaseRegistrationTask;
import org.dosomething.letsdothis.ui.fragments.RegisterLoginFragment;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterLoginActivity extends BaseActivity
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
        registerReceiver(loginReceiver, new IntentFilter(LOGIN_SUCCESS));

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, RegisterLoginFragment.newInstance(),
                         RegisterLoginFragment.TAG).commit();
        }
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(loginReceiver);
        super.onDestroy();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(BaseRegistrationTask task)
    {
        finish();
    }

}
