package org.dosomething.letsdothis.ui;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.BaseFragment;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(savedInstanceState == null)
        {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, BaseFragment.newInstance()).commit();
        }
    }
}
