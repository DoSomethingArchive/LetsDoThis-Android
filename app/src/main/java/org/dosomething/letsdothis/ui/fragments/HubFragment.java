package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.BaseRegistrationTask;
import org.dosomething.letsdothis.tasks.GetCurrentUserCampaignTask;
import org.dosomething.letsdothis.tasks.GetPastUserCampaignTask;
import org.dosomething.letsdothis.ui.UserListActivity;
import org.dosomething.letsdothis.ui.UserProfileActivity;
import org.dosomething.letsdothis.ui.UserUpdateActivity;
import org.dosomething.letsdothis.ui.adapters.CampaignAdapter;
import org.dosomething.letsdothis.ui.adapters.HubAdapter;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class HubFragment extends Fragment
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG = HubFragment.class.getSimpleName();
    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private RecyclerView recyclerView;
    private HubAdapter   adapter;

    public static HubFragment newInstance()
    {
        return new HubFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_hub, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBusExt.getDefault().register(this);
        String currentUserId = AppPrefs.getInstance(getActivity()).getCurrentUserId();
        TaskQueue.loadQueueDefault(getActivity())
                .execute(new GetCurrentUserCampaignTask(currentUserId));
        TaskQueue.loadQueueDefault(getActivity())
                .execute(new GetPastUserCampaignTask(currentUserId));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);

        User user = new User(null, "firstName", "lastName", "birthday");
        adapter = new HubAdapter(user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.logout:
                BaseRegistrationTask.logout(getActivity());
                getActivity().finish();
                break;
            case R.id.user_list:
                startActivity(UserListActivity.getLaunchIntent(getActivity()));
                break;
            case R.id.edit_user:
                startActivity(UserUpdateActivity.getLaunchIntent(getActivity()));
                break;
            case R.id.one_user:
                startActivity(UserProfileActivity.getLaunchIntent(getActivity()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetCurrentUserCampaignTask task)
    {
        if(! task.campaignList.isEmpty())
        {
            adapter.addCurrentCampaign(task.campaignList);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetPastUserCampaignTask task)
    {
        if(! task.campaignList.isEmpty())
        {
            adapter.addPastCampaign(task.campaignList);
        }
    }

}
