package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.UpdateUserTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/15/15.
 */
public class ChangeNumberActivity extends BaseActivity
{
    private static final String TAG = ChangeNumberActivity.class.getSimpleName();

    //~=~=~=~=~=~=~=~=~=~=~=~=Views

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, ChangeNumberActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);

        intiChangeListener();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Change Number");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void intiChangeListener()
    {
        final EditText phone = (EditText) findViewById(R.id.phone);
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                User user = new User();
                user.id = AppPrefs.getInstance(v.getContext()).getCurrentUserId();
                user.mobile = phone.getText().toString();

                TaskQueue.loadQueueDefault(v.getContext()).execute(new UpdateUserTask(user));
            }
        });
    }

}
