package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.network.models.RequestReportback;
import org.dosomething.letsdothis.tasks.ReportbackUploadTask;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.io.File;

import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by toidiu on 5/8/15.
 */
public class ReportBackUploadActivity extends AppCompatActivity
{
    public static final  String CROPPED_SQUARE = "CROPPED_SQUARE";
    public static final  String EXTRA_TITLE    = "EXTRA_TITLE";
    private static final String EXTRA_CAM_ID   = "EXTRA_CAM_ID";
    private int campaignId;

    public static Intent getLaunchIntent(Context context, String cropedSquare, String title, int id)
    {
        Intent intent = new Intent(context, ReportBackUploadActivity.class);
        intent.putExtra(CROPPED_SQUARE, cropedSquare);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CAM_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_back_upload);

        campaignId = getIntent().getIntExtra(EXTRA_CAM_ID, - 1);
        if(BuildConfig.DEBUG && campaignId == - 1)
        {
            Toast.makeText(this, "Error with Campaign", Toast.LENGTH_SHORT).show();
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String croppedImage = getIntent().getStringExtra(CROPPED_SQUARE);
        initView(croppedImage);
    }

    private void initView(String croppedImage)
    {
        ImageView image = (ImageView) findViewById(R.id.image);
        ImageView imageHero = (ImageView) findViewById(R.id.image_hero);
        Picasso.with(this).load(new File(croppedImage)).into(image);
        Picasso.with(this).load(new File(croppedImage)).into(imageHero);

        final EditText caption = (EditText) findViewById(R.id.caption);
        final EditText number = (EditText) findViewById(R.id.number);
        if(BuildConfig.DEBUG)
        {
            caption.setText("test");
            number.setText("8");
        }
        //FIXME-get actual data
        number.setHint("this is the hint");


        this.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RequestReportback req = new RequestReportback();
                req.caption = caption.getText().toString();
                req.why_participated = "";
                int i = 0;
                try
                {
                    i = Integer.parseInt(number.getText().toString());
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(ReportBackUploadActivity.this, "Enter a valid number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                req.quantity = number.getText().toString();
                req.uid = AppPrefs.getInstance(ReportBackUploadActivity.this).getCurrentUserId();

                TaskQueue.loadQueueDefault(ReportBackUploadActivity.this)
                        .execute(new ReportbackUploadTask(req, campaignId));
//                finish();
            }
        });
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
}
