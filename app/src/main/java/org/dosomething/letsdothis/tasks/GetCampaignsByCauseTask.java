package org.dosomething.letsdothis.tasks;

import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.Causes;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.PhoenixAPI;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;

import java.util.ArrayList;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Retrieves campaigns from the Phoenix API tagged with a specified cause.
 *
 * Created by juy on 1/26/16.
 */
public class GetCampaignsByCauseTask extends BaseNetworkErrorHandlerTask {

    private final String mCauseName;
    private ArrayList<Campaign> mResults;

    public GetCampaignsByCauseTask(final String causeName) {
        mCauseName = causeName;
        mResults = new ArrayList<>();
    }

    @Override
    protected void run(Context context) throws Throwable {
        PhoenixAPI api = NetworkHelper.getPhoenixAPIService();

        int id = Causes.getId(mCauseName);
        int page = 1;
        boolean completed = false;
        while (! completed) {
            ResponseCampaignList response = api.campaignListByCause(id, page);
            if (response != null) {
                mResults.addAll(response.getCampaigns(true));

                if (response.getCurrentPage() > 0 &&
                        response.getCurrentPage() < response.getTotalPages()) {
                    completed = false;
                    page++;
                }
                else {
                    completed = true;
                }
            }
            else {
                completed = true;
            }
        }
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

    public ArrayList<Campaign> getResults() {
        return mResults;
    }
}
