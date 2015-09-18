package org.dosomething.letsdothis.tasks;

import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by juy on 9/11/15.
 */
public class DbGetCampaignTask extends Task {

    private final String campaignId;
    public Campaign campaign;

    public DbGetCampaignTask(String id) {
        this.campaignId = id;
    }

    @Override
    protected void run(Context context) throws Throwable {
        campaign = DatabaseHelper.getInstance(context).getCampDao().queryForId(campaignId);
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        return false;
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
