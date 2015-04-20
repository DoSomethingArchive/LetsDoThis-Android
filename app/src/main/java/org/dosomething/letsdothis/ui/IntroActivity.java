package org.dosomething.letsdothis.ui;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import org.dosomething.letsdothis.R;

/**
 * Created by toidiu on 4/15/15.
 */
public class IntroActivity extends ActionBarActivity
{
    private static final String TAG = IntroActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
//        EventBusExt.getDefault().register(this);

        initAppNavigation();

    }

    private void initAppNavigation()
    {
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(LoginActivity.getLaunchIntent(IntroActivity.this));
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(SignupActivity.getLaunchIntent(IntroActivity.this));
            }
        });
    }


    //    @Override
//    protected void onDestroy()
//    {
//        EventBusExt.getDefault().unregister(this);
//        super.onDestroy();
//    }

}
