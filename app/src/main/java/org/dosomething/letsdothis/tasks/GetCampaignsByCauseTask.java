package org.dosomething.letsdothis.tasks;

import android.content.Context;

import org.dosomething.letsdothis.data.Causes;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.PhoenixAPI;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Retrieves campaigns from the Phoenix API tagged with a specified cause.
 *
 * Created by juy on 1/26/16.
 */
public class GetCampaignsByCauseTask extends BaseNetworkErrorHandlerTask {

    private final String mCauseName;
    private ResponseCampaignList mResults;

    public GetCampaignsByCauseTask(final String causeName) {
        mCauseName = causeName;
    }

    @Override
    protected void run(Context context) throws Throwable {
        PhoenixAPI api = NetworkHelper.getPhoenixAPIService();

        int id = Causes.getId(mCauseName);
        mResults = api.campaignListByCause(id);
    }

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);

        EventBusExt.getDefault().post(this);
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        return super.handleError(context, throwable);
    }

    public ResponseCampaignList getResults() {
        return mResults;
    }
}
