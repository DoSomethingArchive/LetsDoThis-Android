package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;
import org.dosomething.letsdothis.network.models.ResponseGroup;
import org.dosomething.letsdothis.ui.adapters.DrawerListAdapter;
import org.dosomething.letsdothis.ui.fragments.ActionsFragment;
import org.dosomething.letsdothis.ui.fragments.HubFragment;
import org.dosomething.letsdothis.ui.fragments.InvitesFragment;
import org.dosomething.letsdothis.ui.fragments.JoinGroupDialogFragment;
import org.dosomething.letsdothis.ui.fragments.NotificationsFragment;
import org.dosomething.letsdothis.ui.fragments.SetTitleListener;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import retrofit.RetrofitError;


public class MainActivity extends BaseActivity implements SetTitleListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String GROUP_ID       = "GROUP_ID";
    public static final String ATTEMPT_INVITE = "ATTEMPT_INVITE";
    //~=~=~=~=~=~=~=~=~=~=~=~=VIEWS
    private Toolbar                               toolbar;


    public static Intent getLaunchIntent(Context context, int groupId, boolean attemptInvite)
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        intent.putExtra(ATTEMPT_INVITE, attemptInvite);
        return intent;
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

        initGroupInvite();
        initToolbar();
        initDrawer();
    }

    private void initGroupInvite()
    {
        boolean attemptInvite = getIntent().getBooleanExtra(ATTEMPT_INVITE, false);
        if(attemptInvite)
        {
            int groupId = getIntent().getIntExtra(GROUP_ID, 0);
            if(groupId == - 1)
            {
                InvitesFragment.showErrorToast(this);
            }
            else
            {
                joinInvite(groupId);
            }
        }
    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Actions");
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                                                                        R.string.invite_code_opt1,
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
                String positionString = list[position];
                if(TextUtils.equals(positionString, getString(R.string.actions)))
                {
                    replaceCurrentFragment(ActionsFragment.newInstance(), ActionsFragment.TAG);

                }
                else if(TextUtils.equals(positionString, getString(R.string.hub)))
                {
                    replaceCurrentFragment(HubFragment.newInstance(null), HubFragment.TAG);

                }
                else if(TextUtils.equals(positionString, getString(R.string.invites)))
                {
                    replaceCurrentFragment(InvitesFragment.newInstance(), InvitesFragment.TAG);

                }
                else if(TextUtils.equals(positionString, getString(R.string.notifications)))
                {
                    replaceCurrentFragment(NotificationsFragment.newInstance(),
                                           NotificationsFragment.TAG);

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

        if(AppPrefs.getInstance(this).isFirstDrawer())
        {
            AppPrefs.getInstance(this).setFirstDrawer();
            drawerLayout.openDrawer(drawer);
        }
    }

    private void joinInvite(final int groupId)
    {
        //fixme one day, we should extract this class so we don't have copy pasta code
        new AsyncTask<Integer, Integer, String[]>()
        {
            @Override
            protected String[] doInBackground(Integer... params)
            {
                String[] responses = new String[2];
                try
                {
                    SystemClock.sleep(1000);

                    Gson gson = new Gson();

                    ResponseGroup responseGroup = NetworkHelper.getNorthstarAPIService()
                                                               .group(params[0]);
                    responses[0] = gson.toJson(responseGroup);

                    ResponseCampaignWrapper responseCampaignWrapper = NetworkHelper
                            .getDoSomethingAPIService().campaign(responseGroup.data.campaign_id);
                    responses[1] = gson.toJson(responseCampaignWrapper);

                }
                catch(RetrofitError | NetworkException e)
                {
                    return null;
                }

                return responses;
            }

            @Override
            protected void onPostExecute(String[] responses)
            {
                super.onPostExecute(responses);
                if(! isCancelled())
                {
                    if(responses == null)
                    {
                        InvitesFragment.showErrorToast(MainActivity.this);
                    }
                    else
                    {
                        JoinGroupDialogFragment joinGroupDialogFragment = JoinGroupDialogFragment
                                .newInstance(groupId, responses[0], responses[1]);
                        joinGroupDialogFragment.show(MainActivity.this.getSupportFragmentManager(),
                                                     JoinGroupDialogFragment.TAG);
                    }
                }
            }
        }.execute(groupId);

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
