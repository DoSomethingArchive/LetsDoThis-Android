package org.dosomething.letsdothis.ui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.ActionsFragment;
import org.dosomething.letsdothis.ui.fragments.Intro0Fragment;
import org.dosomething.letsdothis.ui.fragments.Intro1Fragment;
import org.dosomething.letsdothis.utils.AppPrefs;

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

        if(AppPrefs.getInstance(this).isLoggedIn())
        {
            goToMain();
        }
        else
        {
            initIntroFragment();
        }
    }

    private void initIntroFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, Intro0Fragment.newInstance(), null).commit();

    }

    private void goToMain()
    {
        startActivity(MainActivity.getLaunchIntent(this));
        finish();
    }

    public void replaceCurrentFragment(Fragment fragment, String tag)
    {
        getSupportFragmentManager().beginTransaction().addToBackStack(tag)
                .replace(R.id.container, fragment, tag).commit();
    }


}
