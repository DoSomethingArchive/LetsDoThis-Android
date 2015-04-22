package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.BaseRegistrationTask;
import org.dosomething.letsdothis.ui.fragments.ActionsFragment;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.InvitesFragment;
import org.dosomething.letsdothis.ui.fragments.NotificationsFragment;


public class MainActivity extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener
{

    private View actions;
    private View hub;
    private View invites;
    private View notifications;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ActionsFragment.newInstance(), ActionsFragment.TAG)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        initBottomBarNav();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.logout:
                BaseRegistrationTask.logout(this);
                finish();
                break;
            case R.id.user_list:
                startActivity(UserListActivity.getLaunchIntent(this));
                break;
            case R.id.edit_user:
                startActivity(UserUpdateActivity.getLaunchIntent(this));
                break;
            case R.id.one_user:
                startActivity(UserProfileActivity.getLaunchIntent(this));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBottomBarNav()
    {
        actions = findViewById(R.id.actions);
        actions.setSelected(true);
        actions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(! actions.isSelected())
                {
                    ActionsFragment fragment = (ActionsFragment) getSupportFragmentManager()
                            .findFragmentByTag(ActionsFragment.TAG);
                    if(fragment == null)//should never happen
                    {
                        fragment = ActionsFragment.newInstance();
                    }

                    replaceCurrentFragment(fragment, ActionsFragment.TAG);
                }
            }
        });

        hub = findViewById(R.id.hub);
        hub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(! hub.isSelected())
                {
                    HubFragment fragment = (HubFragment) getSupportFragmentManager()
                            .findFragmentByTag(HubFragment.TAG);
                    if(fragment == null)
                    {
                        fragment = HubFragment.newInstance();
                    }

                    replaceCurrentFragment(fragment, HubFragment.TAG);
                }
            }
        });

        invites = findViewById(R.id.invites);
        invites.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(! invites.isSelected())
                {
                    InvitesFragment fragment = (InvitesFragment) getSupportFragmentManager()
                            .findFragmentByTag(InvitesFragment.TAG);
                    if(fragment == null)
                    {
                        fragment = InvitesFragment.newInstance();
                    }

                    replaceCurrentFragment(fragment, InvitesFragment.TAG);
                }
            }
        });

        notifications = findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(! notifications.isSelected())
                {
                    NotificationsFragment fragment = (NotificationsFragment) getSupportFragmentManager()
                            .findFragmentByTag(NotificationsFragment.TAG);
                    if(fragment == null)
                    {
                        fragment = NotificationsFragment.newInstance();
                    }

                    replaceCurrentFragment(fragment, NotificationsFragment.TAG);
                }
            }
        });
    }

    private void replaceCurrentFragment(Fragment fragment, String tag)
    {
        if(! getSupportFragmentManager().popBackStackImmediate(tag, 0))
        {
            getSupportFragmentManager().beginTransaction().addToBackStack(tag)
                    .replace(R.id.container, fragment, tag).commit();
            updateNavBar();
        }
    }

    @Override
    public void onBackStackChanged()
    {
        updateNavBar();
    }

    private void updateNavBar()
    {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        String currentFragTag = currentFragment.getTag();

        actions.setSelected(TextUtils.equals(ActionsFragment.TAG, currentFragTag));
        notifications.setSelected(TextUtils.equals(NotificationsFragment.TAG, currentFragTag));
        hub.setSelected(TextUtils.equals(HubFragment.TAG, currentFragTag));
        invites.setSelected(TextUtils.equals(InvitesFragment.TAG, currentFragTag));

    }


}
