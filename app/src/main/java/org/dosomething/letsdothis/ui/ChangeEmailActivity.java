package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.UpdateUserTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class ChangeEmailActivity extends BaseActivity
{
    private static final String TAG = ChangeEmailActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, ChangeEmailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        intiChangeListener();
    }

    private void intiChangeListener()
    {
        final EditText email = (EditText) findViewById(R.id.email);
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                User user = new User();
                user.id = AppPrefs.getInstance(v.getContext()).getCurrentUserId();
                user.email = email.getText().toString();

                TaskQueue.loadQueueDefault(v.getContext()).execute(new UpdateUserTask(user));
            }
        });
    }

}
