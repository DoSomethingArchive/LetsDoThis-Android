package org.dosomething.letsdothis.ui;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.dosomething.letsdothis.tasks.LogoutTask;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public abstract class BaseActivity extends AppCompatActivity
{

    public static final String            LOGOUT_SUCCESS = "ship it";
    protected           BroadcastReceiver logoutReceiver = new BroadcastReceiver()
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
        EventBusExt.getDefault().register(this);
    }


    @Override
    protected void onDestroy()
    {
        unregisterReceiver(logoutReceiver);
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    public static void logOutUser(Context context)
    {
        TaskQueue.loadQueueDefault(context).execute(new LogoutTask());
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(LogoutTask task)
    {
        sendBroadcast(new Intent(LOGOUT_SUCCESS));
        startActivity(RegisterLoginActivity.getLaunchIntent(this));
    }
}
