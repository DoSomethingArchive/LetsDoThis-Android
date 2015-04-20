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
import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.ui.adapters.CampaignAdapter;

import java.util.Arrays;

/**
 * Created by izzyoji :) on 4/14/15.
 */
public class CategoryFragment extends Fragment
{

    private static final Integer[] SAMPLE_DATA = {5236, 2544, 362, 28};

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

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        CampaignAdapter adapter = new CampaignAdapter(Arrays.asList(SAMPLE_DATA),
                                                      new CampaignAdapter.CampaignClickListener()
                                                      {
                                                          @Override
                                                          public void onCampaignClicked(Integer campaignId)
                                                          {
                                                              startActivity(CampaignDetailsActivity
                                                                                    .getLaunchIntent(
                                                                                            getActivity(),
                                                                                            campaignId));
                                                          }
                                                      });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}
