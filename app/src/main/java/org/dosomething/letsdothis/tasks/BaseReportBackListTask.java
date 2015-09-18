package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;

import java.util.List;

/**
 * Created by izzyoji :) on 4/23/15.
 */
public abstract class BaseReportBackListTask extends BaseNetworkErrorHandlerTask {

    // Query values for status parameter
    public static String STATUS_PROMOTED = "promoted";
    public static String STATUS_APPROVED = "approved";

    // Number of reportbacks to retrieve with each request
    private final int REQUEST_COUNT = 20;

    public List<ReportBack> reportBacks;
    public int page;
    public int totalPages;
    public String status;

    // If an error is received in the response, this will be populated
    public String error;

    private String campaignIds;

    /**
     * Base task for retrieving reportback items.
     *
     * @param campaignId Campaign(s) to retrieve reportbacks for
     * @param page Page number of paginated response to retrieve
     * @param status Type of reportback items to retrieve - STATUS_PROMOTED or STATUS_APPROVED
     */
    public BaseReportBackListTask(String campaignId, int page, String status) {
        this.campaignIds = campaignId;
        this.page = page;
        this.status = status;
    }

    @Override
    protected void run(Context context) throws Throwable {
        ResponseReportBackList response = NetworkHelper.getDoSomethingAPIService().reportBackList(
            status, campaignIds, REQUEST_COUNT, false, page);

        if (response.error != null) {
            error = response.error.message;
        }
        else {
            totalPages = response.pagination.total_pages;
            reportBacks = ResponseReportBackList.getReportBacks(response);
        }
    }

}
