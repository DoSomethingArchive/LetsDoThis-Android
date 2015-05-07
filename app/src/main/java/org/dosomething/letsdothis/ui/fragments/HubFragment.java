package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.GetCurrentUserCampaignTask;
import org.dosomething.letsdothis.tasks.GetPastUserCampaignTask;
import org.dosomething.letsdothis.ui.SettingsActivity;
import org.dosomething.letsdothis.ui.UserListActivity;
import org.dosomething.letsdothis.ui.UserProfileActivity;
import org.dosomething.letsdothis.ui.UserUpdateActivity;
import org.dosomething.letsdothis.ui.adapters.HubAdapter;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class HubFragment extends AbstractQuickReturnFragment implements HubAdapter.HubAdapterClickListener
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String TAG = HubFragment.class.getSimpleName();

    private HubAdapter         adapter;
    private SetToolbarListener setToolbarListener;

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
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        setToolbarListener = (SetToolbarListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        EventBusExt.getDefault().register(this);
        String currentUserId = AppPrefs.getInstance(getActivity()).getCurrentUserId();
        TaskQueue.loadQueueDefault(getActivity())
                .execute(new GetCurrentUserCampaignTask(currentUserId));
        TaskQueue.loadQueueDefault(getActivity())
                .execute(new GetPastUserCampaignTask(currentUserId));

        View rootView = inflater
                .inflate(R.layout.activity_fragment_quickreturn_recycler, container, false);
        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        TextView title = (TextView) rootView.findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        title.setText(getString(R.string.app_name));
        setToolbarListener.setToolbar(toolbar);

        return rootView;
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
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);

        User user = new User(null, "firstName", "lastName", "birthday");
        adapter = new HubAdapter(user, this);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.fragment_hub, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.settings:
                startActivity(SettingsActivity.getLaunchIntent(getActivity()));
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

    @Override
    public void groupClicked(int campaignId, String userId)
    {
        Toast.makeText(getActivity(), "FIXME", Toast.LENGTH_SHORT).show();
    }


    public interface SetToolbarListener
    {
         abstract void setToolbar(Toolbar toolbar);

    }
}
