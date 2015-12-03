package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.CampaignActions;
import org.dosomething.letsdothis.data.Kudos;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.BaseReportBackListTask;
import org.dosomething.letsdothis.tasks.CampaignDetailsTask;
import org.dosomething.letsdothis.tasks.CampaignSignUpTask;
import org.dosomething.letsdothis.tasks.IndividualCampaignReportBackList;
import org.dosomething.letsdothis.tasks.RbShareDataTask;
import org.dosomething.letsdothis.tasks.ReportbackUploadTask;
import org.dosomething.letsdothis.tasks.SubmitKudosTask;
import org.dosomething.letsdothis.ui.adapters.CampaignDetailsAdapter;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

import java.io.File;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignDetailsActivity extends AppCompatActivity implements CampaignDetailsAdapter.DetailsAdapterClickListener
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_CAMPAIGN_ID = "campaign_id";
    public static final int    SELECT_PICTURE    = 23123;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private Uri                    imageUri;
    private CampaignDetailsAdapter adapter;
    private int                    totalPages;
    private int currentPage = 1;
    private String currentRbQueryStatus;
    //    private ResponseCampaign.ReportBackInfo rBInfo;

    // Google Analytics tracker
    private Tracker mTracker;

    public static Intent getLaunchIntent(Context context, int campaignId)
    {
        return new Intent(context, CampaignDetailsActivity.class)
                .putExtra(EXTRA_CAMPAIGN_ID, campaignId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_quickreturn_recycler);
        EventBusExt.getDefault().register(this);

        mTracker = ((LDTApplication)getApplication()).getDefaultTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int campaignId = getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1);
        boolean userIsSignedUp = false;

        // Determine if user is signed up for this campaign
        try {
            if (CampaignActions.queryForId(this, campaignId) != null) {
                userIsSignedUp = true;
            }
        }
        catch (Exception e) {
            Log.e("CampaignActions", "Exception while checking database for campaign id: " + campaignId);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new CampaignDetailsAdapter(this, getResources(), userIsSignedUp);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Refresh the campaign's information from the server
        refreshCampaign(campaignId);

        // First query for only "promoted" reportbacks to show in the reportback feed
        currentRbQueryStatus = BaseReportBackListTask.STATUS_PROMOTED;
        IndividualCampaignReportBackList task = new IndividualCampaignReportBackList(
                Integer.toString(campaignId), currentPage, BaseReportBackListTask.STATUS_PROMOTED);
        TaskQueue.loadQueueDefault(this).execute(task);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(TaskQueueHelper
                .hasTasksOfType(ReportbackUploadTask.getQueue(this), ReportbackUploadTask.class))
        {
            adapter.processingUpload();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onScrolledToBottom() {
        boolean getMoreData = false;
        if (currentRbQueryStatus == BaseReportBackListTask.STATUS_PROMOTED && totalPages > 0) {
            getMoreData = true;

            if (currentPage >= totalPages) {
                currentPage = 0;
                currentRbQueryStatus = BaseReportBackListTask.STATUS_APPROVED;
            }
        }
        else if (currentRbQueryStatus == BaseReportBackListTask.STATUS_APPROVED && currentPage < totalPages) {
            getMoreData = true;
        }

        if (getMoreData) {
            fetchMoreReportbacks();
        }
    }

    /**
     * Fetches more reportback submissions. Uses current page and status to determine what to fetch
     * next. Should occur when gallery scrolling nears the bottom or if no reportbacks are promoted.
     */
    private void fetchMoreReportbacks() {
        String campaigns = Integer.toString(getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1));
        IndividualCampaignReportBackList task = new IndividualCampaignReportBackList(campaigns,
                currentPage + 1, currentRbQueryStatus);
        TaskQueue.loadQueueDefault(CampaignDetailsActivity.this).execute(task);
    }

    @Override
    public void proveClicked()
    {
        choosePicture();
    }

    @Override
    public void shareClicked(Campaign campaign) {
        TaskQueue.loadQueueDefault(this).execute(new RbShareDataTask(campaign));

        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_BEHAVIOR, AnalyticsUtils.ACTION_SHARE_PHOTO);
    }

    @Override
    public void inviteClicked()
    {
        Campaign campaign = adapter.getCampaign();
        startActivity(
                CampaignInviteActivity.getLaunchIntent(this, campaign.title, campaign.signupGroup));
    }

    @Override
    public void onUserClicked(String id)
    {
        startActivity(PublicProfileActivity.getLaunchIntent(this, id));
    }

    @Override
    public void onKudosClicked(ReportBack reportBack, Kudos kudos)
    {
        TaskQueue.loadQueueDefault(this).execute(new SubmitKudosTask(kudos.id, reportBack.id));
    }

    /**
     * Run when the signup button is clicked.
     *
     * Implements CampaignDetailsAdapter.DetailsAdapterClickListener
     *
     * @param campaignId
     */
    @Override
    public void onSignupClicked(int campaignId) {
         TaskQueue.loadQueueDefault(this).execute(new CampaignSignUpTask(campaignId));
    }

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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

                Uri selectedImageUri = null;
                if (isCamera) {
                    selectedImageUri = imageUri;
                }
                else if (data != null) {
                    selectedImageUri = data.getData();
                }

                Campaign clickedCampaign = adapter.getCampaign();
                if (selectedImageUri != null) {
                    Intent cropIntent = PhotoCropActivity.getResultIntent(
                            this,
                            selectedImageUri.toString(),
                            clickedCampaign.title,
                            clickedCampaign.id);
                    startActivityForResult(cropIntent, PhotoCropActivity.RESULT_CODE);
                }
                else {
                    Toast.makeText(CampaignDetailsActivity.this, R.string.error_photo_select, Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == PhotoCropActivity.RESULT_CODE) {
                String filePath = data.getStringExtra(PhotoCropActivity.RESULT_FILE_PATH);
                Campaign clickedCampaign = adapter.getCampaign();
                String format = String.format(getString(R.string.reportback_upload_hint), clickedCampaign.noun,
                                clickedCampaign.verb);
                startActivity(ReportBackUploadActivity
                                      .getLaunchIntent(this, filePath, clickedCampaign.title,
                                                       clickedCampaign.id, format));
            }
        }
    }

    public void choosePicture()
    {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = new File(Environment.getExternalStorageDirectory(), "DoSomething");
        externalFile.mkdirs();
        File file = new File(externalFile, "reportBack" + System.currentTimeMillis() + ".jpg");
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        imageUri = Uri.parse(file.getAbsolutePath());
        if(BuildConfig.DEBUG)
        {
            Log.d("photo location", imageUri.toString());
        }

        String pickTitle = getString(R.string.select_picture);
        Intent chooserIntent = Intent.createChooser(takePhotoIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    private void refreshCampaign(int campaignId)
    {
        TaskQueue.loadQueueDefault(this).execute(new CampaignDetailsTask(campaignId));
    }

    /**
     * Sends screen view to analytics.
     *
     * This one's a little less than ordinary since we're not sending the screen hit in onResume().
     * We want to know the state of this view when we track this screen, and that requires we wait
     * for several async tasks to return first before we send.
     *
     * @param campaignId int Campaign id
     * @param isSignedUp boolean User is signed up for this campaign
     * @param isComplete boolean User has submitted a reportback for this campaign
     */
    private void sendScreenViewToAnalytics(int campaignId, boolean isSignedUp, boolean isComplete) {
        String state;
        if (isSignedUp) {
            if (isComplete) {
                state = "completed";
            }
            else {
                state = "proveit";
            }
        }
        else {
            state = "pitch";
        }

        String screenName = String.format(AnalyticsUtils.SCREEN_CAMPAIGN, campaignId, state);
        AnalyticsUtils.sendScreen(mTracker, screenName);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportbackUploadTask task)
    {
        refreshCampaign(task.getCampaignId());
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CampaignDetailsTask task) {
        int campaignId = -1;
        boolean isComplete = false;
        boolean isSignedUp = false;

        if(task.campaign != null) {
            // Update the view in the adapter
            adapter.updateCampaign(task.campaign);

            // Set vars for the tracker
            campaignId = task.campaign.id;

            try {
                if (CampaignActions.queryForId(this, campaignId) != null) {
                    isSignedUp = true;
                }
            }
            catch (Exception e) {
                isSignedUp = false;
            }

            isComplete = task.campaign.showShare == Campaign.UploadShare.SHARE;
        }
        else {
            Toast.makeText(this, "campaign data failed", Toast.LENGTH_SHORT).show();
        }

        sendScreenViewToAnalytics(campaignId, isSignedUp, isComplete);
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
    public void onEventMainThread(IndividualCampaignReportBackList task)
    {
        totalPages = task.totalPages;
        currentPage = task.page;
        List<ReportBack> reportBacks = task.reportBacks;
        if (reportBacks != null) {
            adapter.addAll(reportBacks);
        }
        else if (currentRbQueryStatus == BaseReportBackListTask.STATUS_PROMOTED) {
            currentPage = 0;
            currentRbQueryStatus = BaseReportBackListTask.STATUS_APPROVED;

            fetchMoreReportbacks();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CampaignSignUpTask task) {
        adapter.refreshOnSignup();

        AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_CAMPAIGN,
                AnalyticsUtils.ACTION_SUBMIT_SIGNUP, Integer.toString(task.getCampaignId()));
    }
}
