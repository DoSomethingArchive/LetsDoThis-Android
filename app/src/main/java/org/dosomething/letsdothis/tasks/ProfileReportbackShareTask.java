package org.dosomething.letsdothis.tasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtils;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Preps a Share action for a reportback from the user's profile.
 *
 * Created by juy on 2/18/16.
 */
public class ProfileReportbackShareTask extends BaseNetworkErrorHandlerTask {
    // Context
    private Context mContext;

    // True if there was an error while executing this task
    private boolean mHasError;

    // Signup action (and reportback) data
    private ResponseProfileSignups.Signup mAction;

    // Reportback item index of photo to share
    private int mRbItemIndex;

    // Data to extra from a ResponseProfileSignups.Signup
    private String mImageUrl;
    private String mNoun;
    private String mVerb;
    private int mQuantity;
    private String mReportbackId;
    private int mReportbackItemId;

    // On-device image file
    private File mFile;

    public ProfileReportbackShareTask(ResponseProfileSignups.Signup action, int rbItemIndex) {
        mAction = action;
        mRbItemIndex = rbItemIndex;
        mHasError = false;
    }

    @Override
    protected void run(Context context) throws Throwable {
        mContext = context;

        if (mAction == null || mAction.campaign == null || mAction.campaign.reportback_info == null
                || mAction.reportback == null || mAction.reportback.reportback_items == null
                || mRbItemIndex < 0 || mRbItemIndex >= mAction.reportback.reportback_items.data.length) {
            throw new ReportbackShareError();
        }

        mNoun = mAction.campaign.reportback_info.noun;
        mVerb = mAction.campaign.reportback_info.verb;
        mQuantity = mAction.reportback.quantity;
        mReportbackId = mAction.reportback.id;

        ReportBack rbItem = mAction.reportback.reportback_items.data[mRbItemIndex];
        mReportbackItemId = rbItem.id;
        mImageUrl = rbItem.getImagePath();

        String filename = "rb_" + mReportbackId + "item_" + mReportbackItemId + ".jpg";
        mFile = getFile(filename);
        if (! mFile.exists()) {
            mFile = downloadAndSaveTile(filename, getInputStream(mImageUrl));
        }
    }

    /**
     * Downloads and saves an image file.
     *
     * @param name
     * @param inputStream
     * @return
     */
    private static File downloadAndSaveTile(String name, InputStream inputStream) {
        File scaledImage = getFile(name);

        try {
            FileOutputStream out = new FileOutputStream(scaledImage);
            IOUtils.copy(inputStream, out);
            out.close();
            inputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return scaledImage;
    }

    /**
     * Creates local file location for the image.
     *
     * @param name Image filename
     * @return File
     */
    @NotNull
    private static File getFile(String name) {
        File filesDir = new File(Environment.getExternalStorageDirectory(), "DoSomething");
        filesDir.mkdirs();

        return new File(filesDir, name);
    }

    /**
     * Creates the input stream for downloading the file from the url.
     *
     * @param url Image URL
     * @return InputStream
     * @throws IOException
     */
    private static InputStream getInputStream(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(NetworkHelper.CONNECT_TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(NetworkHelper.READ_TIMEOUT, TimeUnit.SECONDS);
        Request request = new Request.Builder().url(url).build();

        Response response;
        InputStream inputStream = null;
        response = client.newCall(request).execute();

        if (response.code() != HttpURLConnection.HTTP_NOT_FOUND) {
            inputStream = response.body().byteStream();
        }

        return inputStream;
    }

    /**
     * Create the Intent for sharing the reportback.
     *
     * @return Intent
     */
    public Intent getShareIntent() {
        String format = mContext.getString(R.string.share_reportback_message);
        String msg = String.format(format, mVerb, mQuantity, mNoun);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, msg);

        // Attach the file if we have it
        if (mFile != null && mFile.exists()) {
            Uri uri = Uri.fromFile(mFile);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        return intent;
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        mHasError = true;

        if (throwable instanceof ReportbackShareError) {
            Toast.makeText(context, context.getString(R.string.error_photo_share), Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return super.handleError(context, throwable);
        }
    }

    public boolean hasError() {
        return mHasError;
    }

    /**
     * Error to throw if there's some data validation error in setting up the share.
     */
    class ReportbackShareError extends RuntimeException {
    }
}
