package org.dosomething.letsdothis.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.fragments.ActionsFragment;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.InvitesFragment;
import org.dosomething.letsdothis.ui.fragments.NotificationsFragment;


public class MainActivity extends ActionBarActivity
{

    private View actions;
    private View hub;
    private View invites;
    private View notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.container, ActionsFragment.newInstance(),
                                            ActionsFragment.TAG).commit();
        }

        actions = findViewById(R.id.actions);
        actions.setSelected(true);
        actions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!actions.isSelected())
                {
                    ActionsFragment fragment = (ActionsFragment) getSupportFragmentManager().findFragmentByTag(ActionsFragment.TAG);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment == null ? ActionsFragment.newInstance() : fragment, ActionsFragment.TAG).commit();
                    actions.setSelected(true);
                    hub.setSelected(false);
                    invites.setSelected(false);
                    notifications.setSelected(false);
                }
            }
        });
        
        hub = findViewById(R.id.hub);
        hub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!hub.isSelected())
                {
                    HubFragment fragment = (HubFragment) getSupportFragmentManager().findFragmentByTag(HubFragment.TAG);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment == null ? HubFragment.newInstance() : fragment, HubFragment.TAG).commit();
                    actions.setSelected(false);
                    hub.setSelected(true);
                    invites.setSelected(false);
                    notifications.setSelected(false);
                }
            }
        });
        
        invites = findViewById(R.id.invites);
        invites.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!invites.isSelected())
                {
                    InvitesFragment fragment = (InvitesFragment) getSupportFragmentManager().findFragmentByTag(
                            InvitesFragment.TAG);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment == null ? InvitesFragment.newInstance() : fragment, InvitesFragment.TAG).commit();
                    actions.setSelected(false);
                    hub.setSelected(false);
                    invites.setSelected(true);
                    notifications.setSelected(false);
                }
            }
        });
        
        notifications = findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!notifications.isSelected())
                {
                    NotificationsFragment fragment = (NotificationsFragment) getSupportFragmentManager().findFragmentByTag(NotificationsFragment.TAG);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment == null ? NotificationsFragment.newInstance() : fragment, NotificationsFragment.TAG).commit();
                    actions.setSelected(false);
                    hub.setSelected(false);
                    invites.setSelected(false);
                    notifications.setSelected(true);
                }
            }
        });
    }

}
