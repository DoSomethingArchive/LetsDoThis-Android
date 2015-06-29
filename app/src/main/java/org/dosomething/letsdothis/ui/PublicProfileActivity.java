package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.NotificationsFragment;

/**
 * Created by toidiu on 5/13/15.
 */
public class PublicProfileActivity extends BaseActivity implements NotificationsFragment.SetTitleListener
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
    }

    @Override
    public void setTitle(String title)
    {
        //dummy interface because we reuse hub fragment for public profile also
    }
}
