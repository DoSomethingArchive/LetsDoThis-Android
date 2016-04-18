package org.dosomething.letsdothis.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.BuildConfig;
import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.network.models.RequestReportback;
import org.dosomething.letsdothis.tasks.ReportbackUploadTask;
import org.dosomething.letsdothis.ui.views.typeface.CustomToolbar;
import org.dosomething.letsdothis.utils.AnalyticsUtils;

import java.io.File;

/**
 * Activity with the form to fill out and submit a report back.
 *
 * Created by toidiu on 5/8/15.
 */
public class ReportBackUploadActivity extends AppCompatActivity
{
    public static final  String FILE_PATH    = "FILE_PATH";
    public static final  String EXTRA_TITLE  = "EXTRA_TITLE";
    private static final String EXTRA_CAM_ID = "EXTRA_CAM_ID";
    private static final String EXTRA_COPY   = "EXTRA_COPY";

    private int campaignId;

    // Google Analytics tracker
    private Tracker mTracker;

    public static Intent getLaunchIntent(Context context, String filePath, String title, int id, String copy)
    {
        Intent intent = new Intent(context, ReportBackUploadActivity.class);
        intent.putExtra(FILE_PATH, filePath);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CAM_ID, id);
        intent.putExtra(EXTRA_COPY, copy);
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
        CustomToolbar toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        String strTitle = getResources().getString(R.string.reportback_toolbar_title_template,
                getIntent().getStringExtra(EXTRA_TITLE));
        toolbar.setTitle(strTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String croppedImage = getIntent().getStringExtra(FILE_PATH);
        initView(croppedImage);

        mTracker = ((LDTApplication)getApplication()).getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Submit screen view to Google Analytics
        String screenName = String.format(AnalyticsUtils.SCREEN_REPORTBACK_FORM, campaignId);
        AnalyticsUtils.sendScreen(mTracker, screenName);
    }

    private void initView(String croppedImage)
    {
        ImageView image = (ImageView) findViewById(R.id.image);
        ImageView imageHero = (ImageView) findViewById(R.id.image_hero);
        Picasso.with(this).load(new File(croppedImage)).into(image);
        Picasso.with(this).load(new File(croppedImage)).into(imageHero);

        final EditText caption = (EditText) findViewById(R.id.caption);
        caption.setHorizontallyScrolling(false);
        caption.setLines(2);

        final EditText number = (EditText) findViewById(R.id.number);
        number.setHint(getIntent().getStringExtra(EXTRA_COPY));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestReportback req = new RequestReportback();
                req.caption = caption.getText().toString();
                if (req.caption.equals("")) {
                    caption.setError(getString(R.string.error_reportback_invalid_caption));
                    return;
                } else if (hasEmoji(req.caption)) {
                    caption.setError(getString(R.string.error_reportback_emoji_caption));
                    return;
                }

                try {
                    Integer.parseInt(number.getText().toString());
                } catch (NumberFormatException e) {
                    number.setError(getString(R.string.error_reportback_invalid_quantity));
                    return;
                }

                req.quantity = number.getText().toString();

                String filePath = getIntent().getStringExtra(FILE_PATH);

                // Upload reportback to server
                ReportbackUploadTask.uploadReport(LDTApplication.getContext(), req, campaignId, filePath);

                // Log to analytics
                AnalyticsUtils.sendEvent(mTracker, AnalyticsUtils.CATEGORY_CAMPAIGN,
                        AnalyticsUtils.ACTION_SUBMIT_REPORTBACK, Integer.toString(campaignId));

                finish();
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

    /**
     * Checks if a string has any emoji characters.
     *
     * @param text String to check
     * @return boolean. True if an emoji is found. Otherwise, false.
     */
    private boolean hasEmoji(String text) {
        for (int i = 0; text != null && i < text.length(); i++) {
            int charType = Character.getType(text.charAt(i));

            // This will also reject some non-emoji characters. But I think they're still in a range
            // that don't really make sense for us to allow in a reportback caption.
            if (charType == Character.SURROGATE || charType == Character.OTHER_SYMBOL) {
                return true;
            }
        }

        return false;
    }
}
