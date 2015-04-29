package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.tasks.CampaignDetailsTask;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;

import java.io.File;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignDetailsActivity extends AppCompatActivity
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String EXTRA_CAMPAIGN_ID = "campaign_id";
    public static final int    SELECT_PICTURE    = 23123;

    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private ImageView image;
    private TextView  title;
    private TextView  callToAction;
    private TextView  problemFact;
    private TextView  solutionCopy;
    private TextView  solutionSupport;
    private Button    proveIt;
    private Uri       imageUri;

    public static Intent getLaunchIntent(Context context, int campaignId)
    {
        return new Intent(context, CampaignDetailsActivity.class)
                .putExtra(EXTRA_CAMPAIGN_ID, campaignId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_details);

        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        callToAction = (TextView) findViewById(R.id.call_to_action);
        problemFact = (TextView) findViewById(R.id.problemFact);
        solutionCopy = (TextView) findViewById(R.id.solutionCopy);
        solutionSupport = (TextView) findViewById(R.id.solutionSupport);
        proveIt = (Button) findViewById(R.id.prove_it);

        View solutionWrapper = findViewById(R.id.solutionWrapper);
        solutionWrapper.setBackground(
                new SlantedBackgroundDrawable(true, getResources().getColor(R.color.web_orange)));
        initListeners();

        EventBusExt.getDefault().register(this);


        TaskQueue.loadQueueDefault(this)
                .execute(new CampaignDetailsTask(getIntent().getIntExtra(EXTRA_CAMPAIGN_ID, - 1)));
    }

    private void initListeners()
    {
        proveIt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                choosePicture();
            }
        });
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
                    selectedImageUri = data == null
                            ? null
                            : data.getData();
                }
                if(BuildConfig.DEBUG)
                {
                    Log.d("asdf-----------", selectedImageUri.toString());
                }
                ImageView viewById = (ImageView) findViewById(R.id.test);
                Picasso.with(this).load(selectedImageUri).into(viewById);
            }
        }
    }


    public void choosePicture()
    {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                     "userPic" + System.currentTimeMillis() + ".jpg");
        imageUri = Uri.fromFile(externalFile);
        if(BuildConfig.DEBUG)
        {
            Log.d("photo location", imageUri.toString());
        }
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        String pickTitle = getString(R.string.select_picture);
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePhotoIntent});

        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CampaignDetailsTask task)
    {
        if(task.campaign != null)
        {
            Campaign campaign = task.campaign;

            Picasso.with(this).load(campaign.imagePath).resize(image.getWidth(), 0).into(image);
            title.setText(campaign.title);
            callToAction.setText(campaign.callToAction);
            problemFact.setText(campaign.problemFact);
            if(BuildConfig.DEBUG && campaign.solutionCopy != null) //FIXME this is null sometime
            {
                String cleanText = campaign.solutionCopy.replace("\n", "");
                solutionCopy.setText(Html.fromHtml(cleanText));
            }
            else
            {
                solutionCopy.setVisibility(View.GONE);
            }
            if(BuildConfig.DEBUG && campaign.solutionSupport != null) //FIXME this is null sometime
            {
                //FIXME also this is a problem. might need to filter the text as soon as we get in from the response.
                Spanned spanned = Html.fromHtml(campaign.solutionSupport);
                String cleanText = spanned.toString().replace("\n", "");
                solutionSupport.setText(cleanText);
            }
            else
            {
                solutionCopy.setVisibility(View.GONE);
            }

        }
        else
        {
            Toast.makeText(this, "campaign data failed", Toast.LENGTH_SHORT).show();
        }
    }
}
