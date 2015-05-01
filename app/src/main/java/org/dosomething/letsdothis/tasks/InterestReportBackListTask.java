package org.dosomething.letsdothis.tasks;
import android.content.Context;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class InterestReportBackListTask extends BaseReportBackListTask
{
    public InterestReportBackListTask(int position, String campaigns, int page)
    {
        super(position, campaigns, page);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
