package org.dosomething.letsdothis.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.models.ResponseCampaign;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;
import org.dosomething.letsdothis.network.models.ResponseGroup;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.tasks.JoinGroupTask;
import org.dosomething.letsdothis.ui.ReportBackDetailsActivity;
import org.dosomething.letsdothis.ui.adapters.JoinGroupAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 7/10/15.
 */
public class JoinGroupDialogFragment extends DialogFragment implements JoinGroupAdapter.JoinGroupAdapterListener
{
    public static final String TAG               = JoinGroupDialogFragment.class.getSimpleName();
    public static final String ARG_GROUP_ID      = "group_id";
    public static final String ARG_GROUP_DATA    = "sweet_data";
    public static final String ARG_CAMPAIGN_DATA = "campaign";
    private Campaign campaign;

    public JoinGroupDialogFragment()
    {
    }

    public static JoinGroupDialogFragment newInstance(int groupId, String groupData, String campaignData)
    {
        JoinGroupDialogFragment joinGroupDialogFragment = new JoinGroupDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GROUP_DATA, groupData);
        args.putString(ARG_CAMPAIGN_DATA, campaignData);
        args.putInt(ARG_GROUP_ID, groupId);
        joinGroupDialogFragment.setArguments(args);
        return joinGroupDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_slide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_recycler_white, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        
        final JoinGroupAdapter adapter = new JoinGroupAdapter(arrangeData(), this);

        final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {

                switch(adapter.getItemViewType(position))
                {
                    case JoinGroupAdapter.VIEW_TYPE_FRIEND:
                        return 1;
                    case JoinGroupAdapter.VIEW_TYPE_REPORT_BACK:
                        return 3;
                    default:
                        return 6;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);

    }

    private ArrayList<Object> arrangeData()
    {
        ArrayList<Object> data = new ArrayList<>();
        Gson gson = new Gson();

        //campaign first
        String campaignString = getArguments().getString(ARG_CAMPAIGN_DATA);
        ResponseCampaignWrapper responseCampaignWrapper = gson.fromJson(campaignString, ResponseCampaignWrapper.class);
        campaign = ResponseCampaign.getCampaign(responseCampaignWrapper.data);
        data.add(campaign);

        String groupString = getArguments().getString(ARG_GROUP_DATA);
        ResponseGroup groupData = gson.fromJson(groupString, ResponseGroup.class);

        List<ResponseUser.Wrapper> responseUserWrappers = Arrays.asList(groupData.data.users);

        //users
        ArrayList<ReportBack> reportBacks = new ArrayList<>();
        for(ResponseUser.Wrapper wrapper : responseUserWrappers)
        {
            ResponseUserCampaign.Wrapper.ResponseReportBackData reportBackData = wrapper.campaigns[0].reportback_data;
            User user = ResponseUser.getUser(wrapper);
            data.add(user);
            if(reportBackData != null)
            {
                reportBacks.addAll(ResponseReportBackList.getReportBacks(reportBackData.reportback_items));
            }
        }

        //join button
        data.add(JoinGroupAdapter.JOIN_PLACEHOLDER);

        //report backs
        data.addAll(reportBacks);

        return data;
    }


    @Override
    public void onFriendClicked(String id)
    {
        Toast.makeText(getActivity(),"TODO are we going to a hub from here", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void joinClicked()
    {
        TaskQueue.loadQueueDefault(getActivity()).execute(new JoinGroupTask(campaign.id, getArguments().getInt(ARG_GROUP_ID)));
        dismiss();
    }

    @Override
    public void closeClicked()
    {
        dismiss();
    }

    @Override
    public void onReportBackClicked(int reportBackId)
    {
        startActivity(ReportBackDetailsActivity.getLaunchIntent(getActivity(), reportBackId));
    }
}
