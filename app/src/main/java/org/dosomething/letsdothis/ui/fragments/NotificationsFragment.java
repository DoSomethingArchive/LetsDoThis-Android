package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.adapters.NotificationListAdapter;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class NotificationsFragment extends Fragment
{
    public static final String TAG = NotificationsFragment.class.getSimpleName();

    public static NotificationsFragment newInstance()
    {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
         inflater.inflate(R.layout.fragment_notifications, container, false);
        ArrayAdapter adapter = new NotificationListAdapter(getActivity(), 0);
        ListView listView = (ListView) container.findViewById(R.id.list);
        listView.setAdapter(adapter);
        adapter.add("");
        adapter.add("");
        adapter.add("");
        adapter.add("");
        adapter.notifyDataSetChanged();
    }


}
