package org.dosomething.letsdothis.ui.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Invite;
import org.dosomething.letsdothis.ui.adapters.InvitesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class InvitesFragment extends Fragment implements InvitesAdapter.InviteAdapterClickListener
{
    public static final String TAG = InvitesFragment.class.getSimpleName();
    private InvitesAdapter adapter;

    public static InvitesFragment newInstance()
    {
        return new InvitesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_fragment_toolbar_recycler, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //FIXME: get real data
        adapter = new InvitesAdapter(generateSampleData(), this);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    }

    private List<Invite> generateSampleData()
    {
        List<Invite> invites = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Invite invite = new Invite();
            invite.title = "Sample invite " + (i + 1);
            invite.details = "This campaign is so cool.";
            invite.code = "555555";
            invites.add(invite);
        }

        return invites;
    }

    @Override
    public void onInviteClicked(String title, String code)
    {
        startActivity(Invite.buildShareIntent(getResources(), title, code));
    }

    @Override
    public void onJoinClicked(String s)
    {
        if(BuildConfig.DEBUG)
        {
            Toast.makeText(getActivity(), "Campaign joined", Toast.LENGTH_SHORT).show();
            //FIXME: get real data
            adapter.showJoinButton(false);
        }
    }

    @Override
    public void onCodeEntered(String code)
    {
        if(BuildConfig.DEBUG)
        {
            if(code.length() == 15)
            {
                //FIXME: get real data
                adapter.showJoinButton(true);
            }
            else
            {
                Toast.makeText(getActivity(), getString(R.string.error_code_length), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

