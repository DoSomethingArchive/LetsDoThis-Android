package org.dosomething.letsdothis.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Causes;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.tasks.GetCampaignsByCauseTask;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Activity displaying campaigns for a single cause.
 *
 * Created by juy on 1/27/16.
 */
public class CauseActivity extends BaseActivity {

    // Intent Bundle extra keys
    private static final String EXTRA_CAUSE_NAME = "cause_name";

    // Name of the cause to display campaigns for
    private String mCauseName;

    // Progress bar view
    private ProgressBar mProgressBar;

    public static Intent getLaunchIntent(Context context, String causeName) {
        return new Intent(context, CauseActivity.class)
                .putExtra(EXTRA_CAUSE_NAME, causeName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cause);

        if (!EventBusExt.getDefault().isRegistered(this)) {
            EventBusExt.getDefault().register(this);
        }

        mCauseName = getIntent().getStringExtra(EXTRA_CAUSE_NAME);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        ImageView titleBg = (ImageView) findViewById(R.id.title_bg);
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView descView = (TextView) findViewById(R.id.description);

        // Extracting rgb values from the color resource so we can also add an alpha to the tint
        int color = getResources().getColor(Causes.getColorRes(mCauseName));
        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = color & 0xff;
        int alpha = 0x77;

        titleBg.setColorFilter(Color.argb(alpha, red, green, blue));
        titleView.setText(mCauseName.toUpperCase());
        descView.setText("TODO: this cause needs a description.");

        GetCampaignsByCauseTask task = new GetCampaignsByCauseTask(mCauseName);
        TaskQueue.loadQueueDefault(this).execute(task);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetCampaignsByCauseTask task) {
        mProgressBar.setVisibility(View.GONE);

        ResponseCampaignList campaigns = task.getResults();

        // @todo Display the campaigns in the RecyclerView
        Log.v("CauseActivity", "campaigns found: " + campaigns.data.length);
    }
}
