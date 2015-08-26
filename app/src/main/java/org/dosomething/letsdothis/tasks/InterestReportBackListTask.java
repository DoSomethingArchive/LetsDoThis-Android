package org.dosomething.letsdothis.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class InterestReportBackListTask extends BaseReportBackListTask {

    // Identifies which tabbed page of campaigns this task is for
    public final int pagerPosition;

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
