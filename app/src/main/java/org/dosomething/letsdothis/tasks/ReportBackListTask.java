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

    public  List<ReportBack> reportBacks;
    public  int              position;
    private String           campaigns;
    public  int              page;
    public  int              totalPages;

    public ReportBackListTask(int position, String campaigns, int page)
    {
        this.position = position;
        this.campaigns = campaigns;
        this.page = page;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseReportBackList response = NetworkHelper.getDoSomethingAPIService()
                                                       .reportBackList(campaigns, 20, false, page);
        totalPages = response.pagination.total_pages;
        reportBacks = ResponseReportBackList.getReportBacks(response);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
