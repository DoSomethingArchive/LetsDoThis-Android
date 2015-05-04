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
    //~=~=~=~=~=~=~=~=~=~=~=~=QuickReturn
    private Toolbar             toolbar;
    private RecyclerView        recycleView;
    private LinearLayoutManager layoutManager;


    public static NotificationsFragment newInstance()
    {
        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_fragment_recycler, container, false);

        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler);
        toolbar = ((MainActivity) getActivity()).toolbar;
        recycleView.setOnScrollListener(getScrollListener());
        return rootView;
    }


    private RecyclerView.OnScrollListener getScrollListener()
    {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener()
        {
            private int toolbarHeight;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if(layoutManager.findFirstVisibleItemPosition() == 0)
                {
                    return;
                }
                if(newState != RecyclerView.SCROLL_STATE_IDLE)
                {
                    return;
                }

                float translate = toolbar.getTranslationY();
                if(toolbarHeight <= - translate)
                {
                    //hide
                    toolbar.setTranslationY(- toolbarHeight);
                }
                else if(translate >= 0)
                {
                    //show
                    toolbar.setTranslationY(0);
                }

                int half = toolbarHeight / 2;
                if(half > - translate)
                {
                    //show
                    toolbar.animate().setDuration(200).translationY(0);
                }
                else
                {
                    //hide
                    toolbar.animate().setDuration(200).translationY(- toolbarHeight);
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                float toolbarTranslate = toolbar.getTranslationY();
                toolbarHeight = toolbar.getHeight();
                boolean hideScroll = dy > 0;

                if(hideScroll)
                {
                    if(toolbarHeight <= - toolbarTranslate)
                    {
                        //hide
                        toolbar.setTranslationY(- toolbarHeight);
                        return;
                    }
                    toolbar.setTranslationY(toolbarTranslate - dy);
                }
                else
                {
                    if(0 <= toolbarTranslate)
                    {
                        //show
                        toolbar.setTranslationY(0);
                        return;
                    }
                    toolbar.setTranslationY(toolbarTranslate - dy);
                }
            }
        };
        return onScrollListener;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //FIXME: get real data
        NotificationAdapter adapter = new NotificationAdapter(generateSampleData());
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
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
