package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Notification;
import org.dosomething.letsdothis.ui.MainActivity;
import org.dosomething.letsdothis.ui.adapters.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_fragment_toolbar_recycler, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //FIXME: get real data
        NotificationAdapter adapter = new NotificationAdapter(generateSampleData());
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private List<Object> generateSampleData()
    {
        List<Object> notifications = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Notification notification = new Notification();
            notification.title = "Sample notification " + (i + 1);
            notification.details = "Here are the fabulous details. ";
            notification.imagePath = "http://icons.iconarchive.com/icons/carlosjj/google-jfk/128/android-icon.png";
            notification.timeStamp = System.currentTimeMillis() - (i * 1000);
            notifications.add(notification);
        }

        return notifications;
    }

}
