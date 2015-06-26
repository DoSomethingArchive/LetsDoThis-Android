package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.adapters.DrawerListAdapter;
import org.dosomething.letsdothis.ui.fragments.ActionsFragment;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.InvitesFragment;
import org.dosomething.letsdothis.ui.fragments.NotificationsFragment;


public class MainActivity extends BaseActivity implements NotificationsFragment.SetTitleListener
{
    public static final String ACTIONS = "Actions";
    public static final String HUB     = "Hub";
    public static final String INVITES = "Invites";
    public static final String NOTIFICATIONS = "Notifications";
    private Toolbar toolbar;

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

        initToolbar();
        initDrawer();
    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                                                        R.string.invite_code_opt,
                                                                        R.string.account);
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    private void initDrawer()
    {
        final String[] list = getResources().getStringArray(R.array.drawer_list);
        final View drawer = findViewById(R.id.drawer);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView listView = (ListView) findViewById(R.id.menu_list);

        listView.setAdapter(new DrawerListAdapter(this, list));
        listView.setItemChecked(0, true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch(list[position])
                {
                    case ACTIONS:
                        replaceCurrentFragment(ActionsFragment.newInstance(), ActionsFragment.TAG);
                        break;
                    case HUB:
                        replaceCurrentFragment(HubFragment.newInstance(false), HubFragment.TAG);
                        break;
                    case INVITES:
                        replaceCurrentFragment(InvitesFragment.newInstance(), InvitesFragment.TAG);
                        break;
                    case NOTIFICATIONS:
                        replaceCurrentFragment(NotificationsFragment.newInstance(),
                                               NotificationsFragment.TAG);
                        break;
                }
                drawerLayout.closeDrawer(drawer);
            }
        });

        View setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(SettingsActivity.getLaunchIntent(MainActivity.this));
                drawerLayout.closeDrawer(drawer);
            }
        });


        if(true)
        {
            drawerLayout.openDrawer(drawer);
        }
    }

    private void replaceCurrentFragment(Fragment fragment, String tag)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void setTitle(String title)
    {
        toolbar.setTitle(title);
    }


}
