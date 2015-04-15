package org.dosomething.letsdothis.ui;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.tasks.LoginTask;
import org.dosomething.letsdothis.ui.fragments.BaseFragment;

/**
 * Created by toidiu on 4/15/15.
 */
public class LoginActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText phone = (EditText) findViewById(R.id.phone);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);

        if(savedInstanceState == null)
        {
        }

        initPasswordListener(password);
    }

    private void initPasswordListener(EditText password)
    {
        password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new LoginTask().callLogin();
            }
        });
    }
}
