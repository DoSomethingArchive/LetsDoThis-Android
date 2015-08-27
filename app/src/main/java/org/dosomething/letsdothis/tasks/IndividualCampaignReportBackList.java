package org.dosomething.letsdothis.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class IndividualCampaignReportBackList extends BaseReportBackListTask {

    /**
     * Task to retrieve reportbacks for a single campaign.
     *
     * @param campaigns Campaign(s) to retrieve reportbacks for
     * @param page Page number of paginated response to retrieve
     * @param status Type of reportback items to retrieve - STATUS_PROMOTED or STATUS_APPROVED
     */
    public IndividualCampaignReportBackList(String campaigns, int page, String status) {
        super(campaigns, page, status);
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

}
