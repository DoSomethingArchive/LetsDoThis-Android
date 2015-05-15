package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.dosomething.letsdothis.R;

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

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(), "TODO", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
