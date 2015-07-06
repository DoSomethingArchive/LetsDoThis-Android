package org.dosomething.letsdothis.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class IndividualCampaignReportBackList extends BaseReportBackListTask
{
    public IndividualCampaignReportBackList(String campaigns, int page)
    {
        super(campaigns, page);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
