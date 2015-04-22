package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.ui.adapters.CampaignAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class CategoryFragment extends Fragment implements CampaignAdapter.CampaignClickListener
{
    private static final Integer[] SAMPLE_DATA_IDS = {5236, 2544, 362, 28};

    private RecyclerView    recyclerView;

    public static CategoryFragment newInstance()
    {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        CampaignAdapter adapter = new CampaignAdapter(generateSampleData(), this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private List<Campaign> generateSampleData()
    {
        List<Campaign> campaigns = new ArrayList<>();
        for(int id : SAMPLE_DATA_IDS)
        {
            Campaign campaign = new Campaign();
            campaign.id = id;
            campaign.imagePath = "https://dosomething-a.akamaihd.net/sites/default/files/images/SocialMediaMakeover_hero_lanscape2.jpg";
            campaign.title = String.format("Sample Campaign %d", id);
            campaign.callToAction = "Call to action.";
            campaign.problemFact = "Problem fact";
            campaigns.add(campaign);
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

}
