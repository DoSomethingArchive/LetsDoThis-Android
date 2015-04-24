package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;

import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/23/15.
 */
public class ReportBackListTask extends BaseNetworkErrorHandlerTask
{

    public List<ReportBack> reportBacks;
    public int position;

    public ReportBackListTask(int position)
    {
        this.position = position;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseReportBackList response = NetworkHelper.getDoSomethingAPIService()
                                                       .reportBackList(null, 20, true, 1);
        reportBacks = ResponseReportBackList.getReportBacks(response);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
