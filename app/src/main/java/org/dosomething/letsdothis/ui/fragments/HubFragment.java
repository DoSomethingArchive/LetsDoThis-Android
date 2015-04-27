package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.BaseRegistrationTask;
import org.dosomething.letsdothis.ui.UserListActivity;
import org.dosomething.letsdothis.ui.UserProfileActivity;
import org.dosomething.letsdothis.ui.UserUpdateActivity;
import org.dosomething.letsdothis.ui.adapters.CampaignAdapter;
import org.dosomething.letsdothis.ui.adapters.HubAdapter;

import java.util.ArrayList;
import java.util.List;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);


        List<Object> hubList = new ArrayList<>();
        hubList.add("bla");

        adapter = new HubAdapter(hubList);
        adapter.addItem("campaign footer");
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                switch(adapter.getItemViewType(position))
                {
                    case CampaignAdapter.VIEW_TYPE_REPORT_BACK:
                        return 1;
                    default:
                        return 2;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
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
}
