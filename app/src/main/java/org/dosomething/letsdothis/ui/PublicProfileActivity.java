package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.HubFragment;

/**
 * Created by toidiu on 5/13/15.
 */
public class PublicProfileActivity extends BaseActivity implements HubFragment.SetToolbarListener
{

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, PublicProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    public void setToolbar(Toolbar toolbar)
    {

    }
}
