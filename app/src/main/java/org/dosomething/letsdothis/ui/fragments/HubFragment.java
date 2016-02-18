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

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.network.models.ResponseProfileCampaign;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.dosomething.letsdothis.tasks.GetProfileSignupsTask;
import org.dosomething.letsdothis.tasks.GetProfileTask;
import org.dosomething.letsdothis.tasks.RbShareDataTask;
import org.dosomething.letsdothis.tasks.ReportbackUploadTask;
import org.dosomething.letsdothis.tasks.UploadAvatarTask;
import org.dosomething.letsdothis.ui.PhotoCropActivity;
import org.dosomething.letsdothis.ui.ReportBackUploadActivity;
import org.dosomething.letsdothis.ui.adapters.HubAdapter;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

import java.io.File;
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

        mTitleListener.setTitle(getResources().getString(R.string.hub));

        String trackerIdentifier;
        String publicId = getArguments().getString(EXTRA_ID, null);

        // Get user's profile info
        TaskQueue taskQueue = TaskQueue.loadQueueDefault(getActivity());
        taskQueue.execute(new GetProfileTask());

        // Get user's campaign activity
        taskQueue.execute(new GetProfileSignupsTask());

        // @todo Evaluate if we actually want to use a blocking progress dialog
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.progress_dialog_hub));
        mProgressDialog.show();

        if (publicId != null) {
            trackerIdentifier = publicId;
        }
        else {
            trackerIdentifier = "self";
        }

        // If a reportback upload is in progress, notify adapter that the upload is processing
        TaskQueue rbQueue = ReportbackUploadTask.getQueue(getActivity());
        boolean rbUploadInProgress = TaskQueueHelper.hasTasksOfType(rbQueue, ReportbackUploadTask.class);
        if (rbUploadInProgress) {
            mAdapter.processingUpload();
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
    public void onShareClicked(Campaign campaign) {
        TaskQueue.loadQueueDefault(getActivity()).execute(new RbShareDataTask(campaign));

        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR, AnalyticsUtils.ACTION_SHARE_PHOTO);
    }

    @Override
    public void onProveClicked(Campaign campaign) {
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
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

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
        if (resultCode == Activity.RESULT_OK) {
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

                Intent photoCropIntent = PhotoCropActivity.getResultIntent(getActivity(),
                        selectedImageUri.toString(), getString(R.string.share_photo), null);
                startActivityForResult(photoCropIntent, PhotoCropActivity.RESULT_CODE);
            }
            else if (requestCode == PhotoCropActivity.RESULT_CODE) {
                String filePath = data.getStringExtra(PhotoCropActivity.RESULT_FILE_PATH);
                Campaign clickedCampaign = mAdapter.getClickedCampaign();
                String format = String.format(getString(R.string.reportback_upload_hint),
                        clickedCampaign.noun, clickedCampaign.verb);

                Intent rbUploadIntent = ReportBackUploadActivity.getLaunchIntent(getActivity(),
                        filePath, clickedCampaign.title, clickedCampaign.id, format);
                startActivity(rbUploadIntent);
            }
        }
    }

    /**
     * Check if tasks are in progress. If they're done, then dismiss the ProgressDialog.
     */
    private void dismissProgressDialogIfDone() {
        TaskQueue defaultQueue = TaskQueue.loadQueueDefault(getActivity());
        boolean hasProfileTask = TaskQueueHelper.hasTasksOfType(defaultQueue, GetProfileTask.class);
        boolean hasSignupsTask = TaskQueueHelper.hasTasksOfType(defaultQueue, GetProfileSignupsTask.class);

        if (!hasProfileTask && !hasSignupsTask) {
            mProgressDialog.dismiss();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(RbShareDataTask task) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

        // Add a default message if we have the data we need
        if (task.getCampaign() != null) {
            String defaultMessage = String.format(getString(R.string.share_reportback_message),
                    task.getCampaign().verb,
                    task.mQuantity,
                    task.getCampaign().noun);
            share.putExtra(Intent.EXTRA_TEXT, defaultMessage);
        }

        // Attach the file if we have it
        if (task.mFile != null && task.mFile.exists()) {
            Uri uri = Uri.fromFile(task.mFile);
            share.putExtra(Intent.EXTRA_STREAM, uri);
        }

        startActivity(share);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportbackUploadTask task) {
        if (!task.hasError()) {
            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.snack),
                    R.string.campaign_reportback_confirmation, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.cerulean_1));
            snackbar.show();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GetProfileTask task) {
        dismissProgressDialogIfDone();

        mAdapter.setUser(task.getResult());
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
                else if (signups.data[i].campaign_run.current) {
                    currentSignups.add(signups.data[i].campaign);
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
