package org.dosomething.letsdothis.tasks;

import android.content.Context;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by juy on 2/16/16.
 */
public class GetProfileSignupsTask extends BaseNetworkErrorHandlerTask {

    // User's Northstar ID, if any
    private String mPublicId;

    // Result from network response
    private ResponseProfileSignups mResult;

    public GetProfileSignupsTask() {
    }

    public GetProfileSignupsTask(String id) {
        mPublicId = id;
    }

    @Override
    protected void run(Context context) throws Throwable {
        NorthstarAPI northstar = NetworkHelper.getNorthstarAPIService();

        // Get signups for the logged-in user
        if (mPublicId == null) {
            String token = AppPrefs.getInstance(context).getSessionToken();
            mResult = northstar.userProfileSignups(token);
        }
        // Get signups of some other user
        else {
            mResult = northstar.getSignupsById(mPublicId);
        }
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);

        super.onComplete(context);
    }

    public ResponseProfileSignups getResult() {
        return mResult;
    }
}
