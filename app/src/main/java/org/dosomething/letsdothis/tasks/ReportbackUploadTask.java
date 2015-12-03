package org.dosomething.letsdothis.tasks;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.RequestReportback;
import org.dosomething.letsdothis.network.models.ResponseSubmitReportBack;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.TaskQueue;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public class ReportbackUploadTask extends BaseNetworkErrorHandlerTask
{
    // Reportback request data
    private final RequestReportback mRequest;

    // Path to the reportback photo
    private final String mFilePath;

    // Campaign ID the reportback is for
    private int mCampaignId;

    // Set to true if an error occurs
    private boolean mHasError;


    public static TaskQueue getQueue(Context context)
    {
        return TaskQueue.loadQueue(context, "reportBack");
    }

    public static void uploadReport(Context context, RequestReportback req, int campaignId, String filePath)
    {
        getQueue(context).execute(new ReportbackUploadTask(req, campaignId, filePath));
    }

    private ReportbackUploadTask(RequestReportback req, int campaignId, String filePath) {
        this.mRequest = req;
        this.mCampaignId = campaignId;
        this.mFilePath = filePath;
    }

    public int getCampaignId() {
        return mCampaignId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        mRequest.file = base64Encode(mFilePath);
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();
        ResponseSubmitReportBack response = NetworkHelper.getNorthstarAPIService()
                .submitReportback(sessionToken, mRequest, mCampaignId);
    }

    private String base64Encode(String filePath)
    {
        Bitmap bm = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);

        if (!mHasError) {
            Toast.makeText(context, R.string.campaign_reportback_confirmation, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        boolean result = super.handleError(context, throwable);
        mHasError = true;
        return result;
    }

}
