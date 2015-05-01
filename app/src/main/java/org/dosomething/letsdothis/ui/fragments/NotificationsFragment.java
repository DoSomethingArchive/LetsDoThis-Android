package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    public static final  String TAG             = NotificationsFragment.class.getSimpleName();
    //~=~=~=~=~=~=~=~=~=~=~=~=QuickReturn
    private static final int    STATE_ONSCREEN  = 0;
    private static final int    STATE_OFFSCREEN = 1;
    private static final int    STATE_RETURNING = 2;
    private Toolbar      toolbar;
    private View         placeholderView;
    private RecyclerView recycleView;
    private int mMinRawY = 0;
    private int mState   = STATE_ONSCREEN;
    private int mQuickReturnHeight;
    private int mMaxScrollY;


    public static NotificationsFragment newInstance()
    {
        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler);
        toolbar = ((MainActivity) getActivity()).toolbar;
        recycleView.setOnScrollListener(getScrollListener());
        recycleView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch(motionEvent.getActionMasked())
                {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("----------", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        Log.d("----------", "up");
                        break;
                }
                return false;
            }
        });
        return rootView;
    }


    private RecyclerView.OnScrollListener getScrollListener()
    {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener()
        {
            boolean hideToolBar = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                float translate = toolbar.getTranslationY();
                Log.d("----------", "translate " + translate);

                //                if(hideToolBar)
                //                {
                //                    ((BaseActivity) getActivity()).getSupportActionBar().hide();
                //                }
                //                else
                //                {
                //                    ((BaseActivity) getActivity()).getSupportActionBar().show();
                //                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                int height = toolbar.getHeight();
                float translate = toolbar.getTranslationY();

                Log.d("----------", "dy " + dy);
                Log.d("----------", "translate " + translate);
                if(dy > 0)
                {
                    if(translate <= - height)
                    {
                        toolbar.setTranslationY(-height);
                        return;
                    }
                    toolbar.setTranslationY(translate - dy);
                    //                    hideToolBar = true;
                }
                else
                {
                    if(translate >= 0)
                    {
                        toolbar.setTranslationY(0);
                        return;
                    }
                    toolbar.setTranslationY(translate - dy);
                    //                    hideToolBar = false;
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        placeholderView = getView().findViewById(R.id.placeholder);

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
