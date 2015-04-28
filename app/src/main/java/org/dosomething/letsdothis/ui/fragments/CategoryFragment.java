package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.ReportBackListTask;
import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.ui.ReportBackDetailsActivity;
import org.dosomething.letsdothis.ui.adapters.CampaignAdapter;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class CategoryFragment extends Fragment implements CampaignAdapter.CampaignAdapterClickListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final Integer[] SAMPLE_DATA_IDS = {15, 48, 50, 362, 955, 1261, 1334, 1273, 1293, 1427, 1429, 1467}; //FIXME this is dev only
    public static final  String    KEY_POSITION    = "position";

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private RecyclerView recyclerView;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private CampaignAdapter    adapter;
    private int                position;
    private ArrayList<Integer> sampleIdsSubset;
    private int                currentPage;
    private int                totalPages;

    public static CategoryFragment newInstance(int position)
    {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        EventBusExt.getDefault().register(this);
        String campaigns = StringUtils.join(sampleIdsSubset, ",");
        TaskQueue.loadQueueDefault(getActivity())
                 .execute(new ReportBackListTask(position, campaigns, 1));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        position = getArguments().getInt(KEY_POSITION);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        adapter = new CampaignAdapter(generateSampleData(), this);
        adapter.addItem("campaign footer");

        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
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

    }

    private List<Campaign> generateSampleData()
    {
        List<Campaign> campaigns = new ArrayList<>();
        sampleIdsSubset = new ArrayList<>();
        for(int i = 0, sample_data_idsLength = SAMPLE_DATA_IDS.length; i < sample_data_idsLength; i++)
        {
            if(i % 4 == position)
            {
                int id = SAMPLE_DATA_IDS[i];
                sampleIdsSubset.add(id);
                Campaign campaign = new Campaign();
                campaign.id = id;
                campaign.imagePath = "https://dosomething-a.akamaihd.net/sites/default/files/images/SocialMediaMakeover_hero_lanscape2.jpg";
                campaign.title = String.format("Sample Campaign %d", id);
                campaign.callToAction = "Call to action.";
                campaign.problemFact = "Problem fact";
                campaigns.add(campaign);
            }

        }

        return campaigns;
    }

    @Override
    public void onCampaignClicked(int campaignId)
    {
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
            ReportBackListTask task = new ReportBackListTask(position,
                                                             StringUtils.join(sampleIdsSubset, ","),
                                                             currentPage + 1);
            TaskQueue.loadQueueDefault(getActivity()).execute(task);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportBackListTask task)
    {
        if(task.position == position)
        {
            totalPages = task.totalPages;
            currentPage = task.page;
            List<ReportBack> reportBacks = task.reportBacks;
            if(reportBacks != null)
            {
                adapter.addAll(reportBacks);
            }
            else
            {
                Toast.makeText(getActivity(), "report back data failed", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
