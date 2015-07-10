package org.dosomething.letsdothis.tasks;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.dosomething.letsdothis.LDTApplication;
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

    private final RequestReportback req;
    private final String            filePath;
    private final int               campaignId;


    public static void uploadReport(Context context, RequestReportback req, int campaignId, String filePath)
    {
        TaskQueue.loadQueue(context, "reportBack")
                .execute(new ReportbackUploadTask(req, campaignId, filePath));
    }

    private ReportbackUploadTask(RequestReportback req, int campaignId, String filePath)
    {
        this.req = req;
        this.campaignId = campaignId;
        this.filePath = filePath;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        req.file = base64Encode(filePath);
        req.why_participated = "Doing something!";
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();
        ResponseSubmitReportBack response = NetworkHelper.getNorthstarAPIService()
                .submitReportback(sessionToken, req, campaignId);
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
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        TaskQueue
                .loadQueueDefault(LDTApplication.getContext()).execute(
                new CampaignDetailsTask(campaignId));
        EventBusExt.getDefault().post(this);
    }

}
