package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.network.models.RequestKudo;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public class SubmitKudosTask extends BaseNetworkErrorHandlerTask
{
    private final int kudosId;
    private final int reportBackId;
    private final String userId;

    public SubmitKudosTask(int kudosId, int reportBackId, String userId)
    {
        this.kudosId = kudosId;
        this.reportBackId = reportBackId;
        this.userId = userId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        RequestKudo requestKudo = new RequestKudo();
        requestKudo.term_ids = new String[] {String.valueOf(kudosId)};
        requestKudo.reportback_item_id = String.valueOf(reportBackId);
        requestKudo.user_id = userId;

//        NetworkHelper.getDoSomethingAPIService().submitKudos(requestKudo);
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }
}
