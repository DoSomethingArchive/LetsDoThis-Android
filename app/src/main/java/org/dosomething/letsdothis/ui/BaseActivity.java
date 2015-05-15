package org.dosomething.letsdothis.ui;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LogoutTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public abstract class BaseActivity extends AppCompatActivity
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int    LIGHTNING_OFFSET = 4;
    public static final String LOGOUT_SUCCESS   = "ship it";
    public static final String LOGIN_SUCCESS    = "walls breached!";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    protected BroadcastReceiver logoutReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    };
    protected BroadcastReceiver loginReceiver  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerReceiver(logoutReceiver, new IntentFilter(LOGOUT_SUCCESS));
        registerReceiver(loginReceiver, new IntentFilter(LOGIN_SUCCESS));

        EventBusExt.getDefault().register(this);
    }


    @Override
    protected void onDestroy()
    {
        unregisterReceiver(logoutReceiver);
        unregisterReceiver(loginReceiver);
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    public static void logOutUser(Context context)
    {
        TaskQueue.loadQueueDefault(context).execute(new LogoutTask());
    }

    public static void broadcastLogInSuccess(Context context)
    {
        context.sendBroadcast(new Intent(LOGIN_SUCCESS));
    }

    protected void initLightning()
    {
        final ImageView lightning = (ImageView) findViewById(R.id.lightning);
        lightning.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        int measuredWidth = getWindow().getDecorView().getMeasuredWidth();
                        int translateX = (- measuredWidth * 3 / LIGHTNING_OFFSET);
                        lightning.setTranslationX(translateX);
                    }
                });
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(AppPrefs fakeTask)
    {
        //EventBus crashes if there is no task registered
    }


}
