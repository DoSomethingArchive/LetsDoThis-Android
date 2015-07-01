package org.dosomething.letsdothis.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.tasks.CampaignGroupDetailsTask;
import org.dosomething.letsdothis.tasks.IndividualCampaignReportBackList;
import org.dosomething.letsdothis.ui.adapters.GroupAdapter;
import org.dosomething.letsdothis.ui.views.GroupReportBackItemDecoration;

import java.io.File;
import java.util.List;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 5/6/15.
 */
public class GroupActivity extends BaseActivity implements GroupAdapter.GroupAdapterClickListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final  String EXTRA_CAMPAIGN_ID = "campaign_id";
    public static final  String EXTRA_USER_ID     = "user_id";
    private static final int    SELECT_PICTURE    = 5095;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private int currentPage = 1;
    private int          totalPages;
    private GroupAdapter adapter;
    private Uri          imageUri;

    public static Intent getLaunchIntent(Context context, int campaignId, String userId)
    {
        return new Intent(context, GroupActivity.class).putExtra(EXTRA_CAMPAIGN_ID, campaignId)
                                                       .putExtra(EXTRA_USER_ID, userId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_group);
        initUI();

        int campaignId = getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1);
        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        if(campaignId != - 1)
        {
            TaskQueue.loadQueueDefault(this)
                     .execute(new CampaignGroupDetailsTask(campaignId, userId));
            TaskQueue.loadQueueDefault(this).execute(
                    new IndividualCampaignReportBackList(Integer.toString(campaignId),
                                                         currentPage));
        }


    }

    private void initUI()
    {
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new GroupReportBackItemDecoration());
        adapter = new GroupAdapter(null, this);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                switch(adapter.getItemViewType(position))
                {
                    case GroupAdapter.VIEW_TYPE_FRIEND:
                        return 1;
                    case GroupAdapter.VIEW_TYPE_REPORT_BACK :
                        return 3;
                    default:
                        return 6;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
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
    public void onReportBackClicked(int reportBackId)
    {
        startActivity(ReportBackDetailsActivity.getLaunchIntent(this, reportBackId));
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
    public void onProveShareClicked()
    {
        //FIXME
        Toast.makeText(this, "FIXME", Toast.LENGTH_SHORT).show();
        choosePicture();
    }

    @Override
    public void onInviteClicked()
    {
        Campaign campaign = adapter.getCampaign();
        startActivity(CampaignInviteActivity.getLaunchIntent(this, campaign.title, campaign.invite.code));
    }

    @Override
    public void onFriendClicked(String id)
    {
        startActivity(PublicProfileActivity.getLaunchIntent(this, id));
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(IndividualCampaignReportBackList task)
    {
        totalPages = task.totalPages;
        currentPage = task.page;
        List<ReportBack> reportBacks = task.reportBacks;
        adapter.addAll(reportBacks);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CampaignGroupDetailsTask task)
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
                                      .getLaunchIntent(this, selectedImageUri.toString()));
            }
        }
    }


    public void choosePicture()
    {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                       android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
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
