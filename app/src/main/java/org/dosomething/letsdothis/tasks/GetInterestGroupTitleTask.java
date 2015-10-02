package org.dosomething.letsdothis.tasks;

import android.content.Context;

import org.dosomething.letsdothis.network.DoSomethingAPI;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseTaxonomyTerm;

import java.util.HashMap;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by juy on 10/1/15.
 */
public class GetInterestGroupTitleTask extends BaseNetworkErrorHandlerTask {

    // Interest group term ids
    final private int[] mGroupIds;

    // Resulting id/name mappings
    public HashMap<Integer, String> mTermResults;

    public GetInterestGroupTitleTask(int id0, int id1, int id2, int id3) {
        mGroupIds = new int[4];
        mTermResults = new HashMap<>();

        mGroupIds[0] = id0;
        mGroupIds[1] = id1;
        mGroupIds[2] = id2;
        mGroupIds[3] = id3;
    }

    @Override
    protected void run(Context context) throws Throwable {
        DoSomethingAPI api = NetworkHelper.getDoSomethingAPIService();

        for (int i = 0; i < 4; i++) {
            ResponseTaxonomyTerm response = api.taxonomyTerm(mGroupIds[i]);
            mTermResults.put(Integer.parseInt(response.tid), response.name);
        }
    }

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);

        EventBusExt.getDefault().post(this);
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        super.handleError(context, throwable);

        return false;
    }
}
