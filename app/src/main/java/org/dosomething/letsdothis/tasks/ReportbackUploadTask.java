package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.RequestReportback;
import org.dosomething.letsdothis.network.models.ResponseReportbackSubmit;
import org.dosomething.letsdothis.network.models.ResponseSubmitReportBack;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.Date;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.client.Response;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public class ReportbackUploadTask extends BaseNetworkErrorHandlerTask
{

    private final RequestReportback req;
    private final int               campaignId;

    public ReportbackUploadTask(RequestReportback req, int campaignId)
    {
        this.req = req;
        this.campaignId = campaignId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        req.file = "test";
        req.why_participated = "Doing something!";
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();
        ResponseSubmitReportBack response = NetworkHelper.getNorthstarAPIService()
                .submitReportback(sessionToken, req, campaignId);
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }
}
