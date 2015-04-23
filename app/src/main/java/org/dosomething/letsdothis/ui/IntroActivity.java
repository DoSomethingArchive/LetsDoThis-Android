package org.dosomething.letsdothis.ui;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.utils.AppPrefs;

/**
 * Created by toidiu on 4/15/15.
 */
public class IntroActivity extends AppCompatActivity
{
    private static final String TAG = IntroActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        //        EventBusExt.getDefault().register(this);

        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            goToMain();
        }
        else
        {
            initAppNavigation();
        }
    }

    private void goToMain()
    {
        startActivity(MainActivity.getLaunchIntent(this));
        finish();
    }

    private void initAppNavigation()
    {
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(LoginActivity.getLaunchIntent(IntroActivity.this));
                finish();
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(SignupActivity.getLaunchIntent(IntroActivity.this));
                finish();
            }
        });
    }

}
