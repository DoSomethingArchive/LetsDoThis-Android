package org.dosomething.letsdothis.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class InterestReportBackListTask extends BaseReportBackListTask {

    // Identifies which tabbed page of campaigns this task is for
    public final int pagerPosition;

    /**
     * Task to retrieve reportbacks for a group of campaigns.
     *
     * @param position Tabbed page position
     * @param campaigns Campaign(s) to retrieve reportbacks for
     * @param page Page number of paginated response to retrieve
     * @param status Type of reportback items to retrieve - STATUS_PROMOTED or STATUS_APPROVED
     */
    public InterestReportBackListTask(int position, String campaigns, int page, String status) {
        super(campaigns, page, status);
        this.pagerPosition = position;
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

}
