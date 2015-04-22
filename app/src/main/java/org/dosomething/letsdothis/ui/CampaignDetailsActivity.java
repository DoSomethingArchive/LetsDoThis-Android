package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
    private ImageView image;
    private TextView  title;
    private TextView  callToAction;
    private TextView  problemFact;
    private TextView  solutionCopy;
    private TextView  solutionSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_details);

        image = (ImageView)findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        callToAction = (TextView) findViewById(R.id.call_to_action);
        problemFact = (TextView) findViewById(R.id.problemFact);
        solutionCopy = (TextView) findViewById(R.id.solutionCopy);
        solutionSupport = (TextView) findViewById(R.id.solutionSupport);

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

            Picasso.with(this).load(campaign.imagePath).resize(image.getWidth(), 0).into(image);
            title.setText(campaign.title);
            callToAction.setText(campaign.callToAction);
            problemFact.setText(campaign.problemFact);
            solutionCopy.setText(Html.fromHtml(campaign.solutionCopy));
            solutionSupport.setText(Html.fromHtml(campaign.solutionSupport));
        }
        else
        {
            Toast.makeText(this, "campaign data failed", Toast.LENGTH_SHORT).show();
        }
    }
}
