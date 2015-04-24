package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class HubFragment extends Fragment
{

    public static final String TAG = HubFragment.class.getSimpleName();

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
