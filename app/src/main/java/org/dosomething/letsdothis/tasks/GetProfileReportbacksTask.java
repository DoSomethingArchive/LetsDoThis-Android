package org.dosomething.letsdothis.tasks;

import android.content.Context;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseProfileReportbacks;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by juy on 2/16/16.
 */
public class GetProfileReportbacksTask extends BaseNetworkErrorHandlerTask {

    private ResponseProfileReportbacks mResult;

    public GetProfileReportbacksTask() {
    }

    @Override
    protected void run(Context context) throws Throwable {
        String token = AppPrefs.getInstance(context).getSessionToken();
        mResult = NetworkHelper.getNorthstarAPIService().userProfileReportbacks(token);
    }

    @Override
    protected void onComplete(Context context) {
        EventBusExt.getDefault().post(this);

        super.onComplete(context);
    }

    public ResponseProfileReportbacks getResult() {
        return mResult;
    }
}
