package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.tasks.CampaignDetailsTask;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignDetailsActivity extends ActionBarActivity
{

    public static final String EXTRA_CAMPAIGN_ID = "campaign_id";
    private TextView  title;
    private TextView  staffPick;
    private TextView callToAction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_details);

        title = (TextView) findViewById(R.id.title);
        staffPick = (TextView) findViewById(R.id.staffPick);
        callToAction = (TextView) findViewById(R.id.callToAction);

        EventBusExt.getDefault().register(this);

        TaskQueue.loadQueueDefault(this)
                 .execute(new CampaignDetailsTask(getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1)));
    }

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    public static Intent getLaunchIntent(Context context, int campaignId)
    {
        return new Intent(context, CampaignDetailsActivity.class).putExtra(EXTRA_CAMPAIGN_ID, campaignId);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CampaignDetailsTask task)
    {
        if(task.campaign != null)
        {
            Campaign campaign = task.campaign;
            title.setText(campaign.title);
            staffPick.setVisibility(campaign.staffPick ? View.VISIBLE : View.GONE);
            callToAction.setText(campaign.callToAction);
        }
        else
        {
            Toast.makeText(this, "campaign data failed", Toast.LENGTH_SHORT).show();
        }
    }
}
