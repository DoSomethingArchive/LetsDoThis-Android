package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.SetTitleListener;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;

/**
 * Activity wrapper for displaying the Hub for a user other than the one who is logged in.
 *
 * Created by toidiu on 5/13/15.
 */
public class PublicProfileActivity extends BaseActivity implements SetTitleListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_ID = "id";

    public static Intent getLaunchIntent(Context context, String id)
    {
        Intent intent = new Intent(context, PublicProfileActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        String id = getIntent().getStringExtra(EXTRA_ID);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, HubFragment.newInstance(id), HubFragment.TAG).commit();
        }

        CustomToolbar toolbar = (CustomToolbar) findViewById(R.id.toolbar);
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
    public void setTitle(String title) {
        CustomToolbar toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }
}
