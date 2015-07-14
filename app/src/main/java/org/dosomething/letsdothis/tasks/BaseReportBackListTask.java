package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;

import java.util.List;

/**
 * Created by izzyoji :) on 4/23/15.
 */
public abstract class BaseReportBackListTask extends BaseNetworkErrorHandlerTask
{

    public  List<ReportBack> reportBacks;
    private String           campaignIds;
    public  int              page;
    public  int              totalPages;

    public BaseReportBackListTask(String campaignId, int page)
    {
        this.campaignIds = campaignId;
        this.page = page;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseReportBackList response = NetworkHelper.getDoSomethingAPIService()
                                                       .reportBackList(campaignIds, 20, false, page);
        //FIXME an issue was created for the server so that an empty array will be returned if empty, not an error
        //letting it crash for now
        totalPages = response.pagination.total_pages;
        reportBacks = ResponseReportBackList.getReportBacks(response);
    }

}
