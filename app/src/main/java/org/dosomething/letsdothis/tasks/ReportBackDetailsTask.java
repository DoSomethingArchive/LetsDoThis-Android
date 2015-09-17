package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseReportBack;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class ReportBackDetailsTask extends BaseNetworkErrorHandlerTask
{
    private final int              reportBackId;
    public        ReportBack       reportBack;

    public ReportBackDetailsTask(int campaignId)
    {
        this.reportBackId = campaignId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseReportBack response = NetworkHelper.getDoSomethingAPIService()
                                                   .reportBack(reportBackId);
        reportBack = ResponseReportBack.getReportBack(response);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
