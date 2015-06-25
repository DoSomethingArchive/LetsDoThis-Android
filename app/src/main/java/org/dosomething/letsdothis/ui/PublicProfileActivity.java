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
    }

    @Override
    public void setTitle(String title)
    {
        //dummy interface because we reuse hub fragment for public profile also
    }
}
