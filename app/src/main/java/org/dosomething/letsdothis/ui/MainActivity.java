package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.CampaignActions;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.dosomething.letsdothis.tasks.GetProfileSignupsTask;
import org.dosomething.letsdothis.ui.adapters.DrawerListAdapter;
import org.dosomething.letsdothis.ui.fragments.CauseListFragment;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.NewsFragment;
import org.dosomething.letsdothis.ui.fragments.ReplaceFragmentListener;
import org.dosomething.letsdothis.ui.fragments.SetTitleListener;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;

import java.sql.SQLException;

import co.touchlab.android.threading.eventbus.EventBusExt;


/**
 * This is the main starting activity for logged-in users. Most other main areas of the app should
 * be made available here as fragments and reachable by the user through the navigation drawer.
 */
public class MainActivity extends BaseActivity implements SetTitleListener, ReplaceFragmentListener {
    private static final String EXTRA_SHOW_FRAGMENT = "EXTRA_SHOW_FRAGMENT";

    //~=~=~=~=~=~=~=~=~=~=~=~=VIEWS
    private CustomToolbar toolbar;
    private DrawerListAdapter drawerListAdapter;

    // Current Fragment being shown
    private Fragment mCurrentFragment;


    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public static Intent getLaunchIntentHubTop(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_SHOW_FRAGMENT, HubFragment.class.getSimpleName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initDrawer();

        String showFrag = getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT);
        if (showFrag != null && showFrag.contentEquals(HubFragment.class.getSimpleName())) {
            replaceCurrentFragment(HubFragment.newInstance(null), HubFragment.TAG);
        }
        else if (savedInstanceState == null) {
            replaceCurrentFragment(NewsFragment.newInstance(), NewsFragment.TAG);
        }

        initToolbar();
        initDrawer();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!EventBusExt.getDefault().isRegistered(this)) {
            EventBusExt.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusExt.getDefault().unregister(this);
    }

    private void initToolbar() {
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Actions");
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                                                        R.string.drawer_desc_open,
                                                                        R.string.drawer_desc_closed);
        drawerLayout.setDrawerListener(mDrawerToggle);
		ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setDisplayHomeAsUpEnabled(true);
			bar.setHomeButtonEnabled(true);
		}
        mDrawerToggle.syncState();
    }

    /**
     * Sets up the navigation drawer with its adapter and its behavior if an item is clicked.
     */
    private void initDrawer() {
        final String[] list = getResources().getStringArray(R.array.drawer_list);
        final View drawer = findViewById(R.id.drawer);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView listView = (ListView) findViewById(R.id.menu_list);

        drawerListAdapter = new DrawerListAdapter(this, list);
        listView.setAdapter(drawerListAdapter);
        drawerListAdapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (drawerListAdapter.selected != position) {
                    drawerListAdapter.selected = position;
                    String positionString = list[position];
                    if (TextUtils.equals(positionString, getString(R.string.nav_news))) {
                        replaceCurrentFragment(NewsFragment.newInstance(), NewsFragment.TAG);
                    } else if (TextUtils.equals(positionString, getString(R.string.actions))) {
                        replaceCurrentFragment(CauseListFragment.newInstance(), CauseListFragment.TAG);
                    } else if (TextUtils.equals(positionString, getString(R.string.hub))) {
                        replaceCurrentFragment(HubFragment.newInstance(null), HubFragment.TAG);
                    }
                }
                drawerLayout.closeDrawer(drawer);
            }
        });

        View setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SettingsActivity.getLaunchIntent(MainActivity.this));
                drawerLayout.closeDrawer(drawer);
            }
        });
    }

    private void replaceCurrentFragment(Fragment fragment, String tag) {
        mCurrentFragment = fragment;

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        drawerListAdapter.notifyDataSetChanged();
    }

    /**
     * Set the Toolbar title.
     *
     * Implements SetTitleListener
     *
     * @param title Toolbar title
     */
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    /**
     * Replace the current fragment with the Actions fragment.
     *
     * Implements ReplaceFragmentListener
     */
    public void replaceWithActionsFragment() {
        final String[] list = getResources().getStringArray(R.array.drawer_list);
        for (int i = 0; i < list.length; i++) {
            if (TextUtils.equals(list[i], getString(R.string.actions))) {
                drawerListAdapter.selected = i;
                break;
            }
        }

        replaceCurrentFragment(CauseListFragment.newInstance(), CauseListFragment.TAG);
    }

    /**
     * Capture the onKeyUp event specifically for use by the React Native NewsFragment.
     *
     * @param keyCode Identifies key to which event applies
     * @param event Event details
     * @return boolean Handled flag
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = false;
        if ((mCurrentFragment != null) && (mCurrentFragment.getTag() != null) &&
				mCurrentFragment.getTag().equals(NewsFragment.TAG)) {
            handled = ((NewsFragment)mCurrentFragment).onKeyUp(keyCode);
        }

        return handled || super.onKeyUp(keyCode, event);
    }

    /**
     * Handle completed tasks that would've been started from LoginActivity and RegisterActivity.
     *
     * @param task GetProfileSignupsTask
     */
    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetProfileSignupsTask task) {
        if (task.getResult() == null) {
            return;
        }

        ResponseProfileSignups signups = task.getResult();
        for (int i = 0; i < signups.data.length; i++) {
            try {
				// Update local cache of actions
                CampaignActions actions = new CampaignActions();
                actions.campaignId = Integer.parseInt(signups.data[i].campaign.id);
                actions.signUpId = Integer.parseInt(signups.data[i].id);
                if (signups.data[i].reportback != null) {
                    actions.reportBackId = Integer.parseInt(signups.data[i].reportback.id);
                }
                CampaignActions.save(MainActivity.this, actions);
            } catch (SQLException e) {
				// Failed to update cache
                Toast.makeText(MainActivity.this, R.string.error_hub_sync, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
				// Failed to process the incoming data, skip this one
				ResponseProfileSignups.Signup failed = signups.data[i];
				if (failed == null) {
					Log.e("MainActivity", String.format("Signup[%d] is null, skipping", i));
				} else if (failed.campaign == null) {
					Log.e("MainActivity", String.format("Signup[%d].campaign is null, id is \"%s\", skipping", i, failed.id));
				} else {
					Log.e("MainActivity", String.format("Signup[%d].id is \"%s\", campaign id \"%s\" invalid, skipping", i, failed.id, failed.campaign.id));
				}
			}
        }
    }
}
