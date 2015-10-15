package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.DbGetCampaignTask;
import org.dosomething.letsdothis.tasks.ReportBackDetailsTask;
import org.dosomething.letsdothis.tasks.SubmitKudosTask;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

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
    private ImageView avatar;
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

    // Google Analytics tracker
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_back_details);

        image = (ImageView) findViewById(R.id.image);
        avatar = (ImageView) findViewById(R.id.avatar);
        location = (TextView) findViewById(R.id.location);
        title = (TextView) findViewById(R.id.title);
        caption = (TextView) findViewById(R.id.caption);
        impact = (TextView) findViewById(R.id.impact);
        name = (TextView) findViewById(R.id.name);

        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTracker = ((LDTApplication)getApplication()).getDefaultTracker();

        TaskQueue.loadQueueDefault(this).execute(
                new ReportBackDetailsTask(getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, -1)));

        TaskQueue.loadQueueDefault(this).execute(
                new DbGetCampaignTask(getIntent().getStringExtra(EXTRA_CAMPAIGN_ID)));
    }

    @Override
    public void onResume() {
        super.onResume();

        // Submit screen view to Google Analytics
        int id = getIntent().getIntExtra(EXTRA_REPORT_BACK_ID, -1);
        String screenName = String.format(AnalyticsUtils.SCREEN_REPORTBACK_ITEM, id);
        AnalyticsUtils.sendScreen(mTracker, screenName);
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
    public void onEventMainThread(ReportBackDetailsTask task) {
        if (task.reportBack == null) {
            Toast.makeText(this, "report back data failed", Toast.LENGTH_SHORT).show();
            return;
        }

        final ReportBack reportBack = task.reportBack;

        // Report back photo
        Picasso.with(this).load(reportBack.getImagePath()).resize(image.getWidth(), 0)
               .into(image);

        // Campaign title
        title.setText(reportBack.campaign.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CampaignDetailsActivity
                        .getLaunchIntent(ReportBackDetailsActivity.this,
                                reportBack.campaign.id));
            }
        });

        // Report back caption
        caption.setText(reportBack.caption);

        // User name
        String username = reportBack.user.first_name;
        if (reportBack.user.last_name != null && !reportBack.user.last_name.isEmpty()) {
            username += " " + reportBack.user.last_name.charAt(0) + ".";
        }
        name.setText(username);

        // User country location
        if (reportBack.user.country != null && !reportBack.user.country.isEmpty()) {
            Locale locale = new Locale("", reportBack.user.country);
            location.setText(locale.getDisplayCountry());
        }

        // User profile photo
        if (reportBack.user.avatarPath != null && !reportBack.user.avatarPath.isEmpty()) {
            Picasso.with(this).load(reportBack.user.avatarPath)
                    .placeholder(R.drawable.default_profile_photo)
                    .resizeDimen(R.dimen.friend_avatar, R.dimen.friend_avatar)
                    .into(avatar);
        }

        toolbar.setTitle(reportBack.campaign.title);

        actionQuantity = String.valueOf(reportBack.reportback.quantity);
        setImpactTextIfReady();

        // Tapping on user's name or picture should open up that user's profile
        OnUserClickListener onUserClickListener = new OnUserClickListener(reportBack.user.id);
        name.setOnClickListener(onUserClickListener);
        avatar.setOnClickListener(onUserClickListener);

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

    /**
     * OnClickListener to open up a user's public profile screen.
     */
    private class OnUserClickListener implements View.OnClickListener {
        private String mUserId;

        public OnUserClickListener(String id) {
            mUserId = id;
        }

        @Override
        public void onClick(View v) {
            Intent i = PublicProfileActivity.getLaunchIntent(ReportBackDetailsActivity.this, mUserId);
            startActivity(i);
        }
    }
}
