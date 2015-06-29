package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.tasks.DbGetUserTask;
import org.dosomething.letsdothis.tasks.GetCurrentUserCampaignTask;
import org.dosomething.letsdothis.tasks.GetPastUserCampaignTask;
import org.dosomething.letsdothis.tasks.GetUserTask;
import org.dosomething.letsdothis.tasks.UploadAvatarTask;
import org.dosomething.letsdothis.ui.CampaignInviteActivity;
import org.dosomething.letsdothis.ui.GroupActivity;
import org.dosomething.letsdothis.ui.PhotoCropActivity;
import org.dosomething.letsdothis.ui.PublicProfileActivity;
import org.dosomething.letsdothis.ui.adapters.HubAdapter;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.io.File;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;


import static org.dosomething.letsdothis.ui.fragments.NotificationsFragment.SetTitleListener;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class HubFragment extends Fragment implements HubAdapter.HubAdapterClickListener
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final  String TAG            = HubFragment.class.getSimpleName();
    private static final int    SELECT_PICTURE = 55;
    public static final  String EXTRA_ID       = "id";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private HubAdapter       adapter;
    private Uri              imageUri;
    private SetTitleListener titleListener;

    public static HubFragment newInstance(String id)
    {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ID, id);
        HubFragment fragment = new HubFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String currentUserId = AppPrefs.getInstance(getActivity()).getCurrentUserId();
        TaskQueue.loadQueueDefault(getActivity())
                .execute(new GetCurrentUserCampaignTask(currentUserId));
        TaskQueue.loadQueueDefault(getActivity())
                .execute(new GetPastUserCampaignTask(currentUserId));
        return inflater.inflate(R.layout.fragment_toolbar_recycler, container, false);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        titleListener = (SetTitleListener) getActivity();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBusExt.getDefault().register(this);
    }

    @Override
    public void onResume()
    {
        titleListener.setTitle("Hub");
        super.onResume();

        String publicId = getArguments().getString(EXTRA_ID, null);
        if(publicId != null)
        {
            TaskQueue.loadQueueDefault(getActivity()).execute(new GetUserTask(publicId));
        }
        else
        {
            DatabaseHelper.defaultDatabaseQueue(getActivity()).execute(
                    new DbGetUserTask(AppPrefs.getInstance(getActivity()).getCurrentUserId()));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);

        String publicId = getArguments().getString(EXTRA_ID, null);
        adapter = new HubAdapter(this, publicId != null);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetCurrentUserCampaignTask task)
    {
        if(! task.campaignList.isEmpty())
        {
            adapter.addCurrentCampaign(task.campaignList);
        }
    }

    @Override
    public void friendClicked(String friendId)
    {
        startActivity(PublicProfileActivity.getLaunchIntent(getActivity(), friendId));
    }

    @Override
    public void groupClicked(int campaignId, String userId)
    {
        startActivity(GroupActivity.getLaunchIntent(getActivity(), campaignId, userId));
    }

    @Override
    public void onProveShareClicked(Campaign campaign)
    {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = new File(
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "report_back" + System.currentTimeMillis() + ".jpg");
        imageUri = Uri.fromFile(externalFile);
        if(BuildConfig.DEBUG)
        {
            Log.d("photo location", imageUri.toString());
        }
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        String pickTitle = getString(R.string.select_picture);
        Intent chooserIntent = Intent.createChooser(takePhotoIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    @Override
    public void onInviteClicked(Campaign campaign)
    {
        startActivity(CampaignInviteActivity.getLaunchIntent(getActivity(), campaign.title,
                                                             campaign.invite.code));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == SELECT_PICTURE)
            {
                final boolean isCamera;
                if(data == null)
                {
                    isCamera = true;
                }
                else
                {
                    final String action = data.getAction();
                    if(action == null)
                    {
                        isCamera = false;
                    }
                    else
                    {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if(isCamera)
                {
                    selectedImageUri = imageUri;
                }
                else
                {
                    selectedImageUri = data.getData();
                }

                startActivity(PhotoCropActivity
                                      .getLaunchIntent(getActivity(), selectedImageUri.toString()));
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetUserTask task)
    {
        adapter.addUser(task.user);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(DbGetUserTask task)
    {
        adapter.addUser(task.user);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UploadAvatarTask task)
    {
        adapter.addUser(task.user);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetPastUserCampaignTask task)
    {
        if(! task.campaignList.isEmpty())
        {
            adapter.addPastCampaign(task.campaignList);
        }
    }
}
