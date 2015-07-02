package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.RequestKudo;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public class SubmitKudosTask extends BaseNetworkErrorHandlerTask
{
    private final int kudosId;
    private final int reportBackId;

    public SubmitKudosTask(int kudosId, int reportBackId)
    {
        this.kudosId = kudosId;
        this.reportBackId = reportBackId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        RequestKudo requestKudo = new RequestKudo();
        requestKudo.kudos_id = kudosId;
        requestKudo.reportback_item_id = reportBackId;

        NetworkHelper.getNorthstarAPIService().submitKudos(requestKudo, AppPrefs
                .getInstance(context).getSessionToken());
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }
}
