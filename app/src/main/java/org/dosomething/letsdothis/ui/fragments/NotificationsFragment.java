package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Notification;
import org.dosomething.letsdothis.ui.adapters.NotificationAdapter;
import org.dosomething.letsdothis.ui.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class NotificationsFragment extends Fragment
{

    public static final String TAG = NotificationsFragment.class.getSimpleName();
    private SetTitleListener titleListener;
    private ProgressBar      progress;

    public static NotificationsFragment newInstance()
    {
        return new NotificationsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        titleListener = (SetTitleListener) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //FIXME: get real data
        NotificationAdapter adapter = new NotificationAdapter(generateSampleData());
        progress = (ProgressBar) getView().findViewById(R.id.progress);
        progress.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.cerulean_1),
                                PorterDuff.Mode.SRC_IN);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        titleListener.setTitle("Notifications");
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
