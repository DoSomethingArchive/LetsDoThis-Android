package org.dosomething.letsdothis.ui;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.tasks.CampaignGroupDetailsTask;
import org.dosomething.letsdothis.tasks.GroupTask;
import org.dosomething.letsdothis.tasks.RbShareDataTask;
import org.dosomething.letsdothis.tasks.ReportbackUploadTask;
import org.dosomething.letsdothis.ui.adapters.GroupAdapter;
import org.dosomething.letsdothis.ui.views.GroupReportBackItemDecoration;

import java.io.File;

import co.touchlab.android.threading.tasks.TaskQueue;
import co.touchlab.android.threading.tasks.utils.TaskQueueHelper;

/**
 * Created by izzyoji :) on 5/6/15.
 */
public class GroupActivity extends BaseActivity implements GroupAdapter.GroupAdapterClickListener
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final  String EXTRA_GROUP_ID = "campaign_id";
    private static final int    SELECT_PICTURE = 5095;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private GroupAdapter adapter;
    private Uri          imageUri;
    private int          groupId;
    private ProgressBar  progressBar;

    public static Intent getLaunchIntent(Context context, int signupGroup)
    {
        return new Intent(context, GroupActivity.class).putExtra(EXTRA_GROUP_ID, signupGroup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_group);
        initUI();

        groupId = getIntent().getIntExtra(EXTRA_GROUP_ID, - 1);

        if(groupId != - 1)
        {
            refresh();
        }

    }

    private void initUI()
    {
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.cerulean_1),
                                PorterDuff.Mode.SRC_IN);

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
                    case GroupAdapter.VIEW_TYPE_REPORT_BACK:
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
    public void onInviteClicked(String title)
    {
        startActivity(CampaignInviteActivity.getLaunchIntent(this, title, groupId));
    }

    @Override
    public void onFriendClicked(String id)
    {
        startActivity(PublicProfileActivity.getLaunchIntent(this, id));
    }

    @Override
    public void onShareClicked(Campaign campaign)
    {
        TaskQueue.loadQueueDefault(this).execute(new RbShareDataTask(campaign));
    }

    @Override
    public void onShowOffClicked(Campaign campaign)
    {
        choosePicture();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
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
                    isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
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

                startActivityForResult(PhotoCropActivity
                                               .getResultIntent(this, selectedImageUri.toString(),
                                                                adapter.getCampaign().title,
                                                                adapter.getCampaign().id),
                                       PhotoCropActivity.RESULT_CODE);

            }
            else if(requestCode == PhotoCropActivity.RESULT_CODE)
            {
                String filePath = data.getStringExtra(PhotoCropActivity.RESULT_FILE_PATH);
                Intent share = new Intent(Intent.ACTION_SEND);

                share.setType("image/*");
                Uri uri = Uri.fromFile(new File(filePath));
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(share);
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

    private void refresh()
    {
        TaskQueue.loadQueueDefault(this).execute(new GroupTask(groupId));
        refreshProgressBar();
    }

    private void refreshProgressBar()
    {
        boolean taskRunning = TaskQueueHelper.hasTasksOfType(TaskQueue.loadQueueDefault(this),
                                                   GroupTask.class);
        if(taskRunning)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
        }
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
    public void onEventMainThread(ReportbackUploadTask task)
    {
        refresh();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(GroupTask task)
    {
        adapter.updateCampaign(task.campaign);
        adapter.addUsers(task.users);
        adapter.addReportBacks(task.reportBacks);
    }
}
