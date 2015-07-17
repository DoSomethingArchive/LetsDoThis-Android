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

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.Kudos;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.CampaignDetailsTask;
import org.dosomething.letsdothis.tasks.IndividualCampaignReportBackList;
import org.dosomething.letsdothis.tasks.RbShareDataTask;
import org.dosomething.letsdothis.tasks.ReportbackUploadTask;
import org.dosomething.letsdothis.tasks.SubmitKudosTask;
import org.dosomething.letsdothis.ui.adapters.CampaignDetailsAdapter;
import org.dosomething.letsdothis.utils.AppPrefs;

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
    //    private ResponseCampaign.ReportBackInfo rBInfo;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new CampaignDetailsAdapter(this, getResources(), AppPrefs.getInstance(this).getCurrentDrupalId());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        int campaignId = getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1);
        refreshCampaign(campaignId);
        TaskQueue.loadQueueDefault(this).execute(
                new IndividualCampaignReportBackList(Integer.toString(campaignId), currentPage));
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
    public void onScrolledToBottom()
    {
        if(currentPage < totalPages)
        {
            if(BuildConfig.DEBUG)
            {
                Toast.makeText(this, "get more data", Toast.LENGTH_SHORT).show();
            }
            String campaigns = Integer.toString(getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1));
            IndividualCampaignReportBackList task = new IndividualCampaignReportBackList(campaigns,
                                                                                         currentPage + 1);
            TaskQueue.loadQueueDefault(this).execute(task);
        }
    }

    @Override
    public void proveClicked()
    {
        choosePicture();
    }

    @Override
    public void shareClicked(Campaign campaign)
    {
        TaskQueue.loadQueueDefault(this).execute(new RbShareDataTask(campaign));
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

    @Override
    protected void onDestroy()
    {
        EventBusExt.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == SELECT_PICTURE)
            {
                final boolean isCamera;
                if(data.getData() == null)
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

                Campaign clickedCampaign = adapter.getCampaign();
                startActivityForResult(PhotoCropActivity
                                               .getResultIntent(this, selectedImageUri.toString(),
                                                                clickedCampaign.title,
                                                                clickedCampaign.id),
                                       PhotoCropActivity.RESULT_CODE);
            }
            else if(requestCode == PhotoCropActivity.RESULT_CODE)
            {
                String filePath = data.getStringExtra(PhotoCropActivity.RESULT_FILE_PATH);
                Campaign clickedCampaign = adapter.getCampaign();
                String format = String
                        .format(getString(R.string.reportback_upload_hint), clickedCampaign.noun,
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

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(ReportbackUploadTask task)
    {
        refreshCampaign(task.campaignId);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CampaignDetailsTask task)
    {
        if(task.campaign != null)
        {
            adapter.updateCampaign(task.campaign);
        }
        else
        {
            Toast.makeText(this, "campaign data failed", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(RbShareDataTask task)
    {
        if(task.file != null && task.file.exists())
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            Uri uri = Uri.fromFile(task.file);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(share);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(IndividualCampaignReportBackList task)
    {
        totalPages = task.totalPages;
        currentPage = task.page;
        List<ReportBack> reportBacks = task.reportBacks;
        adapter.addAll(reportBacks);
    }
}
