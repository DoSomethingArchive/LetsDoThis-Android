package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.CampaignGroupDetailsTask;
import org.dosomething.letsdothis.tasks.IndividualCampaignReportBackList;
import org.dosomething.letsdothis.ui.adapters.GroupAdapter;
import org.dosomething.letsdothis.ui.views.GroupReportBackItemDecoration;

import java.util.List;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 5/6/15.
 */
public class GroupActivity extends BaseActivity implements GroupAdapter.GroupAdapterClickListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_CAMPAIGN_ID = "campaign_id";
    public static final String EXTRA_USER_ID     = "user_id";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private int currentPage = 1;
    private int totalPages;
    private GroupAdapter adapter;

    public static Intent getLaunchIntent(Context context, int campaignId, String userId)
    {
        return new Intent(context, GroupActivity.class).putExtra(EXTRA_CAMPAIGN_ID, campaignId)
                                                       .putExtra(EXTRA_USER_ID, userId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_group);
        initUI();

        int campaignId = getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1);
        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        if(campaignId != - 1)
        {
            TaskQueue.loadQueueDefault(this).execute(new CampaignGroupDetailsTask(campaignId, userId));
            TaskQueue.loadQueueDefault(this).execute(
                    new IndividualCampaignReportBackList(- 1, Integer.toString(campaignId),
                                                         currentPage));
        }


    }

    private void initUI()
    {
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new GroupReportBackItemDecoration());
        adapter = new GroupAdapter(generateSampleData(), this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                switch(adapter.getItemViewType(position))
                {
                    case GroupAdapter.VIEW_TYPE_FRIEND:
                        return 1;
                    case GroupAdapter.VIEW_TYPE_REPORT_BACK :
                        return 3;
                    default:
                        return 6;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
    }

    private Campaign generateSampleData()
    {
        return null;
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

    @Override
    public void onReportBackClicked(int reportBackId)
    {
        startActivity(ReportBackDetailsActivity.getLaunchIntent(this, reportBackId));
    }

    @Override
    public void onScrolledToBottom()
    {
        if(currentPage < totalPages)
        {
            if(BuildConfig.DEBUG)
            {
                Toast.makeText(this, "get more data", Toast.LENGTH_SHORT).show();
            }
            String campaigns = Integer.toString(getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1));
            IndividualCampaignReportBackList task = new IndividualCampaignReportBackList(- 1,
                                                                                         campaigns,
                                                                                         currentPage + 1);
            TaskQueue.loadQueueDefault(this).execute(task);
        }
    }

    @Override
    public void onProveShareClicked()
    {
        Toast.makeText(this, "FIXME", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInviteClicked()
    {
        Toast.makeText(this, "FIXME", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFriendClicked(String id)
    {
        Toast.makeText(this, "FIXME", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(IndividualCampaignReportBackList task)
    {
        totalPages = task.totalPages;
        currentPage = task.page;
        List<ReportBack> reportBacks = task.reportBacks;
        adapter.addAll(reportBacks);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CampaignGroupDetailsTask task)
    {
        if(task.campaign != null)
        {
            adapter.updateCampaign(task.campaign);
        }
        else
        {
            Toast.makeText(this, "campaign data failed", Toast.LENGTH_SHORT).show();
        }
    }
}
