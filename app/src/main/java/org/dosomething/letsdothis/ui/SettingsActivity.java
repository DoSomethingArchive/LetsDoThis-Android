package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LogoutTask;
import org.dosomething.letsdothis.ui.fragments.SetTitleListener;
import org.dosomething.letsdothis.ui.fragments.SettingsFragment;

/**
 * Created by izzyoji :) on 4/29/15.
 */
public class SettingsActivity extends BaseActivity implements SetTitleListener
{

    private Toolbar toolbar;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(LogoutTask task)
    {
        sendBroadcast(new Intent(BaseActivity.LOGOUT_SUCCESS));
        startActivity(RegisterLoginActivity.getLaunchIntent(this));
    }

    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void setTitle(String title)
    {
        toolbar.setTitle(title);
    }
}
