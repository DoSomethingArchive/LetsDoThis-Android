package org.dosomething.letsdothis.ui;
/**
 * Created by toidiu on 5/7/15.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.views.BitmapUtils;
import org.dosomething.letsdothis.ui.views.PhotoSortrView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kgalligan
 * Date: 10/27/13
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PhotoCropActivity extends AppCompatActivity
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final  String CONTENT_URI_PREFIX = "content://";
    public static final  String IMAGE_PATH         = "IMAGE_PATH";
    public static final  String EXTRA_TITLE        = "EXTRA_TITLE";
    public static final  String EXTRA_CAMP_ID      = "EXTRA_CAMP_ID";
    public static final  int    RESULT_FAILED      = 100;
    private static final int    DEFAULT_MAX_SIZE   = 640;
    public static final  int    RESULT_CODE        = 324;
    public static final  String RESULT_FILE_PATH   = "file_path";
    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private boolean        mPhotoLoaded;
    private Toolbar        toolbar;
    //~=~=~=~=~=~=~=~=~=~=~=~=Views
    private PhotoSortrView photoCropView;
    private FrameLayout    transparency;
    private int            imageWidth;

    public static Intent getLaunchIntent(Context context, String path, String title, Integer campaignId)
    {
        Intent intent = new Intent(context, PhotoCropActivity.class);
        intent.putExtra(IMAGE_PATH, path);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CAMP_ID, campaignId);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_crop_photo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoCropView = (PhotoSortrView) findViewById(R.id.photo_crop_view);
        transparency = (FrameLayout) findViewById(R.id.transparency);

        final long createdTime = System.currentTimeMillis();

        photoCropView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        photoCropView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        setTransparencyHeight();

                        String filePath = getIntent().getStringExtra(IMAGE_PATH);

                        new AsyncTask<String, Void, Bitmap>()
                        {
                            @Override
                            protected Bitmap doInBackground(String... params)
                            {
                                String path = params[0];
                                int preferredSize = photoCropView
                                        .getMeasuredWidth() > DEFAULT_MAX_SIZE
                                        ? DEFAULT_MAX_SIZE
                                        : photoCropView.getMeasuredWidth();

                                Bitmap raw;
                                if(path.startsWith(CONTENT_URI_PREFIX))
                                {
                                    //If we were passed a URI path, we need to decode it from there,
                                    //which is probably from the network, so show a loading spinner
                                    publishProgress();
                                    raw = BitmapUtils
                                            .decodeSampledBitmapFromURI(PhotoCropActivity.this,
                                                                        Uri.parse(path),
                                                                        preferredSize,
                                                                        preferredSize);
                                }
                                else
                                {
                                    //Otherwise we just decode it locally which should be super fast
                                    raw = BitmapUtils
                                            .decodeSampledBitmapFromPath(path, preferredSize,
                                                                         preferredSize);
                                }

                                if(raw != null)
                                {
                                    return BitmapUtils.rotateBitmap(path, raw);
                                }
                                else
                                {
                                    return null;
                                }
                            }

                            @Override
                            protected void onProgressUpdate(Void... params)
                            {
                                //                                mLoadingView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            protected void onPostExecute(Bitmap bitmap)
                            {
                                if(bitmap == null)
                                {
                                    // We should only get here if we don't have network and
                                    // we had to download the image
                                    Runnable exitRunnable = new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            setResult(RESULT_FAILED);
                                            onBackPressed();
                                        }
                                    };
                                    long elapsedTime = System.currentTimeMillis() - createdTime;
                                    long timeRemaining = 600 - elapsedTime;
                                    if(timeRemaining > 0)
                                    {
                                        new Handler().postDelayed(exitRunnable, timeRemaining);
                                    }
                                    else
                                    {
                                        exitRunnable.run();
                                    }
                                    return;
                                }

                                photoCropView.addImage(new BitmapDrawable(getResources(), bitmap));
                                photoCropView.invalidate();
                                mPhotoLoaded = true;
                            }
                        }.execute(filePath);
                    }
                });

    }

    private void setTransparencyHeight()
    {
        int h = photoCropView.getMeasuredHeight();
        imageWidth = photoCropView.getMeasuredWidth();

        //assume height bigger than width
        int i = h - imageWidth;
        ViewGroup.LayoutParams layoutParams = transparency.getLayoutParams();
        layoutParams.height = i;
        transparency.setLayoutParams(layoutParams);
    }

    public void saveReportBackImage(View save)
    {
        if(mPhotoLoaded)
        {
            Bitmap bitmap = null;
            File scaledAvatar = new File(getFilesDir(),
                                         "squareReportBack_" + System.currentTimeMillis() + ".jpg");

            try
            {
                bitmap = photoCropView.takeSquareScreenshot(imageWidth);

                FileOutputStream out = new FileOutputStream(scaledAvatar);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.close();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }

            Intent intent = new Intent().putExtra(RESULT_FILE_PATH, scaledAvatar.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
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

}