package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.GetUserTask;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/16/15.
 */
public class UserProfileActivity extends ActionBarActivity
{
    public static void callMe(Context context)
    {
        Intent intent = new Intent(context, UserProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        EventBusExt.getDefault().register(this);

        TaskQueue.loadQueueDefault(this).execute(new GetUserTask());
    }

    private void updateUI(User user)
    {
        TextView email = (TextView) findViewById(R.id.email);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView first = (TextView) findViewById(R.id.first_name);
        TextView last = (TextView) findViewById(R.id.last_name);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetUserTask task)
    {
        updateUI(task.user);
    }


}
