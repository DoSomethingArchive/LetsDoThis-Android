package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.GetUserTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/16/15.
 */
public class UserProfileActivity extends ActionBarActivity
{
    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, UserProfileActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        EventBusExt.getDefault().register(this);

        String id = AppPrefs.getInstance(this).getCurrentUserId();
        TaskQueue.loadQueueDefault(this).execute(new GetUserTask(id));
    }

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    private void updateUI(User user)
    {
        TextView id = (TextView) findViewById(R.id.id);
        TextView email = (TextView) findViewById(R.id.email);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView first = (TextView) findViewById(R.id.first_name);
        TextView last = (TextView) findViewById(R.id.last_name);
        TextView birthday = (TextView) findViewById(R.id.birthday);
        id.setText(user.id);
        email.setText(user.email);
        phone.setText(user.mobile);
        first.setText(user.first_name);
        last.setText(user.last_name);
        birthday.setText(user.birthdate);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetUserTask task)
    {
        updateUI(task.user);
    }


}
