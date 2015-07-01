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
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.tasks.GetCurrentUserCampaignTask;
import org.dosomething.letsdothis.tasks.GetPastUserCampaignTask;
import org.dosomething.letsdothis.ui.CampaignInviteActivity;
import org.dosomething.letsdothis.ui.GroupActivity;
import org.dosomething.letsdothis.ui.PhotoCropActivity;
import org.dosomething.letsdothis.ui.PublicProfileActivity;
import org.dosomething.letsdothis.ui.adapters.HubAdapter;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.io.File;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class HubFragment extends Fragment implements HubAdapter.HubAdapterClickListener
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final  String TAG            = HubFragment.class.getSimpleName();
    private static final int    SELECT_PICTURE = 55;
    public static final  String PUBLIC_PROFILE = "PUBLIC_PROFILE";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private HubAdapter       adapter;
    private Uri              imageUri;
    private SetTitleListener titleListener;

    public static HubFragment newInstance(boolean isPublic)
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean(PUBLIC_PROFILE, isPublic);
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
        EventBusExt.getDefault().register(this);
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
        //FIXME I don't think this is the right place for this
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);

        //FIXME get real user
        User user = new User(null, "firstName", "lastName", "birthday");
        user.id = AppPrefs.getInstance(getActivity()).getCurrentUserId();

        boolean isPublic = getArguments().getBoolean(PUBLIC_PROFILE);
        if(isPublic)
        {
            user.first_name = "public";
        }

        adapter = new HubAdapter(user, this, isPublic);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        titleListener.setTitle("Hub");
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetCurrentUserCampaignTask task)
    {
        if(! task.campaignList.isEmpty())
        {
            adapter.addCurrentCampaign(task.campaignList);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetPastUserCampaignTask task)
    {
        if(! task.campaignList.isEmpty())
        {
            adapter.addPastCampaign(task.campaignList);
        }
    }

    @Override
    public void friendClicked(String friendId)
    {
        startActivity(PublicProfileActivity.getLaunchIntent(getActivity()));
    }

    @Override
    public void groupClicked(int campaignId, String userId)
    {
        startActivity(GroupActivity.getLaunchIntent(getActivity(), campaignId, userId));
    }

    @Override
    public void onProveShareClicked(Campaign campaign)
    {
        choosePicture();
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


    public void choosePicture()
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


}
