package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.SetTitleListener;

/**
 * Created by toidiu on 5/13/15.
 */
public class PublicProfileActivity extends BaseActivity implements SetTitleListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_ID = "id";
    private Toolbar toolbar;
    
    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, PublicProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, HubFragment.newInstance(true), HubFragment.TAG).commit();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    @Override
    public void setTitle(String title)
    {
        //        toolbar.setTitle(title); PLACEHOLDER IN PUBLIC HUB

    }
}