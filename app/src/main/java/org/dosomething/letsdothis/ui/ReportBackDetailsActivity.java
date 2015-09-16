package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Kudos;
import org.dosomething.letsdothis.data.KudosMeta;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.ReportBackDetailsTask;
import org.dosomething.letsdothis.tasks.SubmitKudosTask;
import org.dosomething.letsdothis.ui.views.KudosView;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class ReportBackDetailsActivity extends BaseActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_REPORT_BACK_ID = "report_back_id";

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ImageView image;
    private TextView  timestamp;
    private TextView  title;
    private TextView  caption;
    private TextView  name;
    private ViewGroup kudos;
    private CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_back_details);

        image = (ImageView) findViewById(R.id.image);
        timestamp = (TextView) findViewById(R.id.timestamp);
        title = (TextView) findViewById(R.id.title);
        caption = (TextView) findViewById(R.id.caption);
        name = (TextView) findViewById(R.id.name);
        kudos = (ViewGroup) findViewById(R.id.kudos_bar);

        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, - 1)));
    }

    public static Intent getLaunchIntent(Context context, int reportBackId)
    {
        return new Intent(context, ReportBackDetailsActivity.class)
                .putExtra(EXTRA_REPORT_BACK_ID, reportBackId);
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

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(SubmitKudosTask task)
    {
        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, - 1)));
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportBackDetailsTask task)
    {
        if(task.reportBack != null)
        {
            final ReportBack reportBack = task.reportBack;
            User user = task.user;

            Picasso.with(this).load(reportBack.getImagePath()).resize(image.getWidth(), 0)
                   .into(image);
            timestamp.setText(TimeUtils.getTimeSince(this, reportBack.createdAt * 1000));
            title.setText(reportBack.campaign.title);
            title.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startActivity(CampaignDetailsActivity
                                          .getLaunchIntent(ReportBackDetailsActivity.this,
                                                           reportBack.campaign.id));
                }
            });
            caption.setText(reportBack.caption);
            if(user != null && ! TextUtils.isEmpty(user.first_name))
            {
                String formattedName = TextUtils.isEmpty(user.last_name)
                        ? user.first_name
                        : String.format("%s %s.", user.first_name, user.last_name.charAt(0));
                name.setText(formattedName);
            }
            else
            {
                name.setText(reportBack.user.id);
            }
            toolbar.setTitle(reportBack.campaign.title);

            //FIXME add user's avatar

            int drupalId = AppPrefs.getInstance(this).getCurrentDrupalId();
            ArrayList<KudosMeta> sanitizedKudosList = reportBack.getSanitizedKudosList(drupalId);
            for(int i = 0, size = sanitizedKudosList.size(); i < size; i++)
            {
                final KudosView kudoView = (KudosView) kudos.getChildAt(i);
                KudosMeta kudosMeta = sanitizedKudosList.get(i);
                kudoView.setKudos(kudosMeta);
                kudoView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Context context = ReportBackDetailsActivity.this;
                        boolean selected = kudoView.isSelected();

                        if(! selected && !reportBack.kudosed)
                        {
                            kudoView.setSelected(true);
                            int countNum = kudoView.getCountNum();
                            kudoView.setCountNum(countNum + 1);
                            Kudos kudos = kudoView.getKudos();
                            TaskQueue.loadQueueDefault(context)
                                     .execute(new SubmitKudosTask(kudos.id, reportBack.id));
                            kudoView.getImage().startAnimation(
                                    AnimationUtils.loadAnimation(context, R.anim.scale_bounce));
                        }

                    }
                });

            }


        }
        else
        {
            Toast.makeText(this, "report back data failed", Toast.LENGTH_SHORT).show();
        }
    }
}
