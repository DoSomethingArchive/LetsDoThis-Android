package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.ReportBackDetailsTask;
import org.dosomething.letsdothis.utils.TimeUtils;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class ReportBackDetailsActivity extends AppCompatActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_REPORT_BACK_ID = "report_back_id";

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ImageView image;
    private TextView  timestamp;
    private TextView  caption;
    private TextView  name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_back_details);

        image = (ImageView) findViewById(R.id.image);
        timestamp = (TextView) findViewById(R.id.timestamp);
        caption = (TextView) findViewById(R.id.caption);
        name = (TextView) findViewById(R.id.name);

        EventBusExt.getDefault().register(this);

        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, - 1)));
    }

    public static Intent getLaunchIntent(Context context, int reportBackId)
    {
        return new Intent(context, ReportBackDetailsActivity.class)
                .putExtra(EXTRA_REPORT_BACK_ID, reportBackId);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportBackDetailsTask task)
    {
        if(task.reportBack != null)
        {
            ReportBack reportBack = task.reportBack;
            User user = task.user;

            Picasso.with(this).load(reportBack.getImagePath()).resize(image.getWidth(), 0).into(image);
            timestamp.setText(TimeUtils.getTimeSince(this, reportBack.createdAt * 1000));
            caption.setText(reportBack.caption);
            if(user != null)
            {
                name.setText(String.format("%s %s.", user.first_name, user.last_name.charAt(0)));
            }
            else
            {
                name.setText(reportBack.user.id);
            }
            setTitle(reportBack.campaign.title);

            //FIXME add user's avatar
            //FIXME add kudos
        }
        else
        {
            Toast.makeText(this, "report back data failed", Toast.LENGTH_SHORT).show();
        }
    }
}
