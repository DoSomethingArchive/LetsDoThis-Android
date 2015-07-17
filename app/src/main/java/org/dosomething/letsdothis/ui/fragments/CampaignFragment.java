package org.dosomething.letsdothis.ui.fragments;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.data.InterestGroup;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.CampaignSignUpTask;
import org.dosomething.letsdothis.tasks.DbInterestGroupCampaignListTask;
import org.dosomething.letsdothis.tasks.InterestReportBackListTask;
import org.dosomething.letsdothis.tasks.UpdateInterestGroupCampaignTask;
import org.dosomething.letsdothis.tasks.UpdateInterestGroupCampaignTask.IdQuery;
import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.ui.ReportBackDetailsActivity;
import org.dosomething.letsdothis.ui.adapters.CampaignAdapter;
import org.dosomething.letsdothis.ui.views.ActionGridSpacingDecoration;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class CampaignFragment extends Fragment implements CampaignAdapter.CampaignAdapterClickListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String KEY_POSITION = "pagerPosition";
    public static final int    FIRST_PAGE   = 1;

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private RecyclerView recyclerView;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private CampaignAdapter adapter;
    private int             position;
    private int             currentPage;
    private int             totalPages;
    private ArrayList<Integer> campaignIds = new ArrayList<>();
    private ProgressBar progress;

    public static CampaignFragment newInstance(int position)
    {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        CampaignFragment campaignFragment = new CampaignFragment();
        campaignFragment.setArguments(args);
        return campaignFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_fragment_recycler, container, false);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        position = getArguments().getInt(KEY_POSITION);
        progress = (ProgressBar) getView().findViewById(R.id.progress);
        progress.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.cerulean_1),
                                PorterDuff.Mode.SRC_IN);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        adapter = new CampaignAdapter(this, getResources());
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.addItemDecoration(new ActionGridSpacingDecoration());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                switch(adapter.getItemViewType(position))
                {
                    case CampaignAdapter.VIEW_TYPE_REPORT_BACK:
                        return 1;
                    default:
                        return 2;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);

        EventBusExt.getDefault().register(this);
        dbCampaignRefresh();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(adapter != null)
        {
            adapter.notifyItemRangeChanged(0, 3);
        }
    }

    @Override
    public void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCampaignClicked(int campaignId)
    {
        TaskQueue.loadQueueDefault(getActivity()).execute(new CampaignSignUpTask(campaignId));
        startActivity(CampaignDetailsActivity.getLaunchIntent(getActivity(), campaignId));
    }

    @Override
    public void onCampaignExpanded(int position)
    {
        recyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void onReportBackClicked(int reportBackId)
    {
        startActivity(ReportBackDetailsActivity.getLaunchIntent(getActivity(), reportBackId));
    }

    @Override
    public void onScrolledToBottom()
    {
        if(currentPage < totalPages)
        {
            if(BuildConfig.DEBUG)
            {
                Toast.makeText(getActivity(), "get more data", Toast.LENGTH_SHORT).show();
            }
            InterestReportBackListTask task = new InterestReportBackListTask(position, StringUtils
                    .join(campaignIds, ","), currentPage + 1);
            getCampaignQueue().execute(task);
        }
    }

    @Override
    public void onNetworkCampaignRefresh()
    {
        adapter.clear();
        currentPage = 0;
        totalPages = 0;
        for(InterestGroup i : InterestGroup.values())
        {
            getCampaignQueue()
                    .execute(new UpdateInterestGroupCampaignTask(i.id));
        }

        refreshProgressBar();
    }

    private TaskQueue getCampaignQueue()
    {
        return TaskQueue.loadQueue(getActivity(), "campaignQueue");
    }


    public void dbCampaignRefresh()
    {
        adapter.clear();
        currentPage = 0;
        totalPages = 0;
        DatabaseHelper.defaultDatabaseQueue(getActivity())
                .execute(new DbInterestGroupCampaignListTask(findGroupId()));
        refreshProgressBar();
    }

    private int findGroupId()
    {
        return InterestGroup.values()[position].id;
    }

    private void refreshProgressBar()
    {
        if(progress != null)
        {
            IdQuery queueQuery = new IdQuery(findGroupId());
            getCampaignQueue().query(queueQuery);

            if(queueQuery.found)
            {
                progress.setVisibility(View.VISIBLE);
            }
            else
            {
                progress.setVisibility(View.GONE);
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(InterestReportBackListTask task)
    {
        if(task.pagerPosition == position && currentPage < task.page)
        {
            totalPages = task.totalPages;
            currentPage = task.page;
            List<ReportBack> reportBacks = task.reportBacks;
            adapter.addAll(reportBacks);
        }

    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(DbInterestGroupCampaignListTask task)
    {
        if(findGroupId() == task.interestGroupId)
        {
            if(task.campList.isEmpty())
            {
                getCampaignQueue()
                        .execute(new UpdateInterestGroupCampaignTask(task.interestGroupId));
            }
            else
            {
                refreshProgressBar();
                adapter.setCampaigns(task.campList);

                campaignIds.clear();
                for(Campaign campaign : task.campList)
                {
                    campaignIds.add(campaign.id);
                }

                String campaigns = StringUtils.join(campaignIds, ",");
                getCampaignQueue()
                        .execute(new InterestReportBackListTask(position, campaigns, FIRST_PAGE));

            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UpdateInterestGroupCampaignTask task)
    {
        getCampaignQueue().execute(new DbInterestGroupCampaignListTask(task.interestGroupId));
    }

}
