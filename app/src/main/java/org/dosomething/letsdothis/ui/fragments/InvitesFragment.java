package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Invite;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;
import org.dosomething.letsdothis.network.models.ResponseGroup;
import org.dosomething.letsdothis.tasks.InvitesTask;
import org.dosomething.letsdothis.tasks.JoinGroupTask;
import org.dosomething.letsdothis.ui.adapters.InvitesAdapter;
import org.dosomething.letsdothis.utils.Hashery;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import retrofit.RetrofitError;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class InvitesFragment extends Fragment implements InvitesAdapter.InviteAdapterClickListener
{
    public static final String TAG = InvitesFragment.class.getSimpleName();
    private InvitesAdapter                        adapter;
    private SetTitleListener                      titleListener;
    private AsyncTask<Integer, Integer, String[]> searchForGroupAsyncTask;
    private ProgressBar                           progress;


    public static InvitesFragment newInstance()
    {
        return new InvitesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        titleListener = (SetTitleListener) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        adapter = new InvitesAdapter(this);
        progress = (ProgressBar) getView().findViewById(R.id.progress);
        progress.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.cerulean_1),
                                PorterDuff.Mode.SRC_IN);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        titleListener.setTitle(getString(R.string.invites));

        refreshInvites();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBusExt.getDefault().register(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onInviteClicked(String title, String code)
    {
        startActivity(Invite.buildShareIntent(getResources(), title, code));
    }

    @Override
    public void onSearchClicked(String code)
    {

        final int groupId = Hashery.getInstance(getActivity()).decode(code);
        if(groupId == - 1)
        {
            showErrorToast();
            adapter.setButtonState(InvitesAdapter.BUTTON_STATE_SEARCH);
        }
        else
        {
            adapter.setButtonState(InvitesAdapter.BUTTON_STATE_CANCEL);
            searchForGroupAsyncTask = new AsyncTask<Integer, Integer, String[]>()
            {
                @Override
                protected String[] doInBackground(Integer... params)
                {
                    String[] responses = new String[2];
                    try
                    {
                        SystemClock.sleep(1000);

                        Gson gson = new Gson();

                        ResponseGroup responseGroup = NetworkHelper.getNorthstarAPIService()
                                                                   .group(params[0]);
                        responses[0] = gson.toJson(responseGroup);

                        ResponseCampaignWrapper responseCampaignWrapper = NetworkHelper
                                .getDoSomethingAPIService()
                                .campaign(responseGroup.data.campaign_id);
                        responses[1] = gson.toJson(responseCampaignWrapper);

                    }
                    catch(RetrofitError | NetworkException e)
                    {
                        return null;
                    }

                    return responses;
                }

                @Override
                protected void onPostExecute(String[] responses)
                {
                    super.onPostExecute(responses);
                    if(! isCancelled())
                    {
                        if(responses == null)
                        {
                            showErrorToast();
                            adapter.setButtonState(InvitesAdapter.BUTTON_STATE_GONE);
                        }
                        else
                        {
                            adapter.setButtonState(InvitesAdapter.BUTTON_STATE_GONE);
                            JoinGroupDialogFragment joinGroupDialogFragment = JoinGroupDialogFragment
                                    .newInstance(groupId, responses[0], responses[1]);
                            joinGroupDialogFragment
                                    .show(getChildFragmentManager(), JoinGroupDialogFragment.TAG);
                        }
                    }
                }
            };
            searchForGroupAsyncTask.execute(groupId);
        }
    }

    private void showErrorToast()
    {
        Toast.makeText(getActivity(), "No campaign found!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelClicked()
    {
        if(searchForGroupAsyncTask != null && searchForGroupAsyncTask
                .getStatus() == AsyncTask.Status.RUNNING)
        {
            searchForGroupAsyncTask.cancel(true);
            adapter.setButtonState(InvitesAdapter.BUTTON_STATE_GONE);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(InvitesTask task)
    {
        adapter.setData(task.invites);
        progress.setVisibility(View.GONE);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(JoinGroupTask task)
    {
        refreshInvites();
    }

    private void refreshInvites()
    {
        progress.setVisibility(View.VISIBLE);
        TaskQueue.loadQueueDefault(getActivity()).execute(new InvitesTask());
    }

}

