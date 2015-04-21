package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.DbGetUser;
import org.dosomething.letsdothis.tasks.UpdateUserTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 4/17/15.
 */
public class UserUpdateActivity extends ActionBarActivity
{

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private EditText password;
    private TextView id;
    private EditText birthdate;

    public static Intent getLaunchIntent(Context context)
    {
        return new Intent(context, UserUpdateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        EventBusExt.getDefault().register(this);

        initSubmitListener();

        String id = AppPrefs.getInstance(this).getCurrentUserId();
        TaskQueue.loadQueueDefault(this).execute(new DbGetUser(id));
    }

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initSubmitListener()
    {
        id = (TextView) findViewById(R.id.id);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        birthdate = (EditText) findViewById(R.id.birthday);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String em = email.getText().toString();
                String ph = phone.getText().toString();
                String pass = password.getText().toString();
                User user = new User(em, ph, pass);
                user.first_name = firstName.getText().toString();
                user.last_name = lastName.getText().toString();
                user.id = id.getText().toString();
                user.birthdate = birthdate.getText().toString();

                TaskQueue.loadQueueDefault(UserUpdateActivity.this)
                        .execute(new UpdateUserTask(user));
            }
        });
    }

    private void updateUI(User user)
    {
        firstName.setText(user.first_name);
        lastName.setText(user.last_name);
        email.setText(user.email);
        phone.setText(user.mobile);
        password.setText(user.password);
        id.setText(user.id);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(DbGetUser task)
    {
        if(task.user != null)
        {
            updateUI(task.user);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UpdateUserTask task)
    {
        if(task.updatedUser != null)
        {
            updateUI(task.updatedUser);
        }
    }


}
