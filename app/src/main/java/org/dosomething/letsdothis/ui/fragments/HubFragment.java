package org.dosomething.letsdothis.ui.fragments;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.soundcloud.android.crop.Crop;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.CampaignActions;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.models.ResponseProfileCampaign;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.dosomething.letsdothis.tasks.GetProfileSignupsTask;
import org.dosomething.letsdothis.tasks.GetProfileTask;
import org.dosomething.letsdothis.tasks.ProfileReportbackShareTask;
import org.dosomething.letsdothis.tasks.ReportbackUploadTask;
import org.dosomething.letsdothis.tasks.UploadAvatarTask;
import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.ui.ReportBackUploadActivity;
import org.dosomething.letsdothis.ui.adapters.HubAdapter;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class HubFragment extends Fragment implements HubAdapter.HubAdapterClickListener {

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final  String TAG            = HubFragment.class.getSimpleName();
    private static final int    SELECT_PICTURE = 52345;
    public static final  String EXTRA_ID       = "id";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private HubAdapter mAdapter;
    private Uri mImageUri;
    private SetTitleListener mTitleListener;
    private ReplaceFragmentListener mReplaceFragmentListener;

    // Google Analytics tracker
    private Tracker mTracker;

    // Progress dialog to display while API calls are in progress
    private ProgressDialog mProgressDialog;

    // Campaign details on a reportback in progress, if any
    private int mRbCampaignId;
    private String mRbCampaignTitle;
    private String mRbCampaignNoun;
    private String mRbCampaignVerb;

    // File destination for a reportback photo submission
    private File mReportbackDestination;

    public static HubFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ID, id);
        HubFragment fragment = new HubFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        LDTApplication application = (LDTApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // Display a progress dialog while the profile is getting synced
        showProgressDialog(R.string.progress_dialog_hub);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mTitleListener = (SetTitleListener) getActivity();

        if (activity instanceof ReplaceFragmentListener) {
            mReplaceFragmentListener = (ReplaceFragmentListener) activity;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusExt.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!EventBusExt.getDefault().isRegistered(this)) {
            EventBusExt.getDefault().register(this);
        }

        mTitleListener.setTitle("");

        String trackerIdentifier;
        String publicId = getArguments().getString(EXTRA_ID, null);
        TaskQueue taskQueue = TaskQueue.loadQueueDefault(getActivity());

        if (publicId != null) {
            // Get other user's profile info and campaign activity
            taskQueue.execute(new GetProfileTask(publicId));
            taskQueue.execute(new GetProfileSignupsTask(publicId));

            trackerIdentifier = publicId;
        }
        else {
            // Get logged-in user's profile info and campaign activity
            taskQueue.execute(new GetProfileTask());
            taskQueue.execute(new GetProfileSignupsTask());

            trackerIdentifier = "self";
        }

        // If a reportback upload is in progress, notify adapter that the upload is processing
        TaskQueue rbQueue = ReportbackUploadTask.getQueue(getActivity());
        boolean rbUploadInProgress = TaskQueueHelper.hasTasksOfType(rbQueue, ReportbackUploadTask.class);
        if (rbUploadInProgress) {
            showProgressDialog(R.string.progress_dialog_hub);
        }

        // Submit screen view to Google Analytics
        String screenName = String.format(AnalyticsUtils.SCREEN_USER_PROFILE, trackerIdentifier);
        AnalyticsUtils.sendScreen(mTracker, screenName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String publicId = getArguments().getString(EXTRA_ID, null);
        mAdapter = new HubAdapter(getActivity(), this, publicId != null);

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onCampaignClicked(int campaignId) {
        Intent intent = CampaignDetailsActivity.getLaunchIntent(getActivity(), campaignId);
        startActivity(intent);
    }

    @Override
    public void onShareClicked(ResponseProfileSignups.Signup completedAction, int rbItemIndex) {
        showProgressDialog(R.string.progress_dialog_wait);

        TaskQueue.loadQueueDefault(getActivity()).execute(new ProfileReportbackShareTask(completedAction, rbItemIndex));

        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR, AnalyticsUtils.ACTION_SHARE_PHOTO);
    }

    @Override
    public void onProveClicked(int campaignId, String campaignTitle, String noun, String verb) {
        // Campaign details to be used through the reportback process
        mRbCampaignId = campaignId;
        mRbCampaignTitle = campaignTitle;
        mRbCampaignNoun = noun;
        mRbCampaignVerb = verb;

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = new File(Environment.getExternalStorageDirectory(), "DoSomething");
        externalFile.mkdirs();
        File file = new File(externalFile, "reportBack" + System.currentTimeMillis() + ".jpg");
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        mImageUri = Uri.parse(file.getAbsolutePath());
        if (BuildConfig.DEBUG) {
            Log.d("photo location", mImageUri.toString());
        }

        String pickTitle = getString(R.string.select_picture);
        Intent chooserIntent = Intent.createChooser(takePhotoIntent, pickTitle);

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    /**
     * When Action button is clicked in an empty Hub, go to the Actions screen.
     *
     * Implements HubAdapter.HubAdapterClickListener
     */
    @Override
    public void onActionsButtonClicked() {
        mReplaceFragmentListener.replaceWithActionsFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == SELECT_PICTURE) {
            final boolean isCamera;
            if (data == null || data.getData() == null) {
                isCamera = true;
            }
            else {
                final String action = data.getAction();
                if (action == null) {
                    isCamera = false;
                }
                else {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }

            Uri selectedImageUri;
            if (isCamera) {
                selectedImageUri = mImageUri;
            }
            else {
                selectedImageUri = data.getData();
            }

            File filesDir = new File(Environment.getExternalStorageDirectory(), "DoSomething");
            filesDir.mkdirs();
            mReportbackDestination = new File(filesDir, "rb" + System.currentTimeMillis() + ".jpg");
            Uri destination = Uri.fromFile(mReportbackDestination);

            Crop.of(selectedImageUri, destination).asSquare().start(getActivity(), HubFragment.this);
        }
        else if (requestCode == Crop.REQUEST_CROP) {
            String filePath = mReportbackDestination.getAbsolutePath();
            String format = String.format(getString(R.string.reportback_upload_hint),
                    mRbCampaignNoun, mRbCampaignVerb);

            Intent rbUploadIntent = ReportBackUploadActivity.getLaunchIntent(getActivity(),
                    filePath, mRbCampaignTitle, mRbCampaignId, format);
            startActivity(rbUploadIntent);
        }
    }

    /**
     * Create and show a ProgressDialog.
     *
     * @param resMessage Message's string resource id
     */
    private void showProgressDialog(int resMessage) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }

        mProgressDialog.setMessage(getString(resMessage));
        mProgressDialog.show();
    }


    /**
     * Check if tasks are in progress. If they're done, then dismiss the ProgressDialog.
     */
    private void dismissProgressDialogIfDone() {
        TaskQueue defaultQueue = TaskQueue.loadQueueDefault(getActivity());
        boolean hasProfileTask = TaskQueueHelper.hasTasksOfType(defaultQueue, GetProfileTask.class);
        boolean hasSignupsTask = TaskQueueHelper.hasTasksOfType(defaultQueue, GetProfileSignupsTask.class);
        boolean hasShareTask = TaskQueueHelper.hasTasksOfType(defaultQueue, ProfileReportbackShareTask.class);

        TaskQueue rbQueue = ReportbackUploadTask.getQueue(getActivity());
        boolean hasUploadTask = TaskQueueHelper.hasTasksOfType(rbQueue, ReportbackUploadTask.class);

        if (!hasProfileTask && !hasSignupsTask && !hasUploadTask && !hasShareTask) {
            mProgressDialog.dismiss();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ProfileReportbackShareTask task) {
        dismissProgressDialogIfDone();

        startActivity(task.getShareIntent());
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportbackUploadTask task) {
        dismissProgressDialogIfDone();

        if (!task.hasError()) {
            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.snack),
                    R.string.campaign_reportback_confirmation, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.cerulean_1));
            snackbar.show();

            // Refresh the campaign list by doing a GetProfileSignupsTask
            TaskQueue.loadQueueDefault(getActivity()).execute(new GetProfileSignupsTask());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetProfileTask task) {
        dismissProgressDialogIfDone();

        User user = task.getResult();
        if (user != null) {
            String first = user.first_name != null ? user.first_name : "";
            String last = "";
            if (user.last_initial != null && ! user.last_initial.isEmpty()) {
                last = user.last_initial + ".";
            }

            mTitleListener.setTitle(String.format("%s %s", first, last).trim());
        }

        mAdapter.setUser(user);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetProfileSignupsTask task) {
        dismissProgressDialogIfDone();

        ArrayList<ResponseProfileCampaign> currentSignups = new ArrayList<>();
        ArrayList<ResponseProfileSignups.Signup> completedActions = new ArrayList<>();
        if (task.getResult() != null) {
            ResponseProfileSignups signups = task.getResult();
            for (int i = 0; i < signups.data.length; i++) {
                // Campaigns that the user has signed up for and reported back on
                if (signups.data[i].reportback != null) {
                    completedActions.add(signups.data[i]);
                }
                // Campaigns that the user has signed up for but has no reportback yet
                // Campaign must be current. All should have a run, but if not, let's just default
                // to showing it in the list.
                else if (signups.data[i].campaign_run == null || signups.data[i].campaign_run.current) {
                    currentSignups.add(signups.data[i].campaign);
                }

                // Update local cache of actions. Skip if displaying a public user profile.
                // If an EXTRA_ID string arg exists, then this is for a public profile.
                if (getArguments().getString(EXTRA_ID, null) == null) {
                    try {
                        CampaignActions actions = new CampaignActions();
                        actions.campaignId = Integer.parseInt(signups.data[i].campaign.id);
                        actions.signUpId = Integer.parseInt(signups.data[i].id);
                        if (signups.data[i].reportback != null) {
                            actions.reportBackId = Integer.parseInt(signups.data[i].reportback.id);
                        }
                        CampaignActions.save(getActivity(), actions);
                    } catch (SQLException e) {
                        Toast.makeText(getActivity(), R.string.error_hub_sync, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        mAdapter.setCurrentSignups(currentSignups);
        mAdapter.setCompletedActions(completedActions);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(UploadAvatarTask task) {
        mAdapter.setUser(task.user);
    }

}
