package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.GetUserListTask;

import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/16/15.
 */
public class UserListActivity extends AppCompatActivity
{
    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, UserListActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        EventBusExt.getDefault().register(this);

        TaskQueue.loadQueueDefault(this).execute(new GetUserListTask());
    }

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    private void updateUI(List<User> userList)
    {
        TextView num = (TextView) findViewById(R.id.user_num);
        num.setText("" + userList.size());
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetUserListTask task)
    {
        updateUI(task.userList);
    }


}
