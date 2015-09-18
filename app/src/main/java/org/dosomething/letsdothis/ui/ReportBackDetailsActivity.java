package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.DbGetCampaignTask;
import org.dosomething.letsdothis.tasks.ReportBackDetailsTask;
import org.dosomething.letsdothis.tasks.SubmitKudosTask;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;

import java.util.Locale;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class ReportBackDetailsActivity extends BaseActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_REPORT_BACK_ID = "report_back_id";
    public static final String EXTRA_CAMPAIGN_ID = "campaign_id";

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ImageView image;
    private TextView  location;
    private TextView  title;
    private TextView  caption;
    private TextView  name;
    private TextView  impact;
    private CustomToolbar toolbar;

    // Values for impact text
    private String actionNoun;
    private String actionVerb;
    private String actionQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_back_details);

        image = (ImageView) findViewById(R.id.image);
        location = (TextView) findViewById(R.id.location);
        title = (TextView) findViewById(R.id.title);
        caption = (TextView) findViewById(R.id.caption);
        impact = (TextView) findViewById(R.id.impact);
        name = (TextView) findViewById(R.id.name);

        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, -1)));

        TaskQueue.loadQueueDefault(this).execute(
                new DbGetCampaignTask(getIntent().getStringExtra(EXTRA_CAMPAIGN_ID)));
    }

    public static Intent getLaunchIntent(Context context, int reportBackId, int campaignId)
    {
        return new Intent(context, ReportBackDetailsActivity.class)
                .putExtra(EXTRA_REPORT_BACK_ID, reportBackId)
                .putExtra(EXTRA_CAMPAIGN_ID, Integer.toString(campaignId));
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

    private void setImpactTextIfReady() {
        if (actionNoun != null && actionVerb != null && actionQuantity != null) {
            impact.setText(String.format("%s %s %s", actionQuantity, actionNoun, actionVerb));
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(SubmitKudosTask task)
    {
        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, -1)));
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportBackDetailsTask task)
    {
        if(task.reportBack != null)
        {
            final ReportBack reportBack = task.reportBack;

            Picasso.with(this).load(reportBack.getImagePath()).resize(image.getWidth(), 0)
                   .into(image);

            if (reportBack.user.country != null && !reportBack.user.country.isEmpty()) {
                Locale locale = new Locale("", reportBack.user.country);
                location.setText(locale.getDisplayCountry());
            }

            title.setText(reportBack.campaign.title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(CampaignDetailsActivity
                            .getLaunchIntent(ReportBackDetailsActivity.this,
                                    reportBack.campaign.id));
                }
            });

            caption.setText(reportBack.caption);

            String username = reportBack.user.first_name;
            if (reportBack.user.last_name != null && !reportBack.user.last_name.isEmpty()) {
                username += " " + reportBack.user.last_name.charAt(0) + ".";
            }
            name.setText(username);

            toolbar.setTitle(reportBack.campaign.title);

            actionQuantity = String.valueOf(reportBack.reportback.quantity);
            setImpactTextIfReady();
        }
        else
        {
            Toast.makeText(this, "report back data failed", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("UnusedDeclarations")
    public void onEventMainThread(DbGetCampaignTask task) {
        if (task == null || task.campaign == null) {
            return;
        }

        actionNoun = task.campaign.noun;
        actionVerb = task.campaign.verb;
        setImpactTextIfReady();
    }
}
