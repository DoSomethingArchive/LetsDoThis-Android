package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.RequestReportback;
import org.dosomething.letsdothis.network.models.ResponseReportbackSubmit;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;
import retrofit.client.Response;

/**
 * Created by izzyoji :) on 5/15/15.
 */
public class ReportbackUploadTask extends BaseNetworkErrorHandlerTask
{

    private final RequestReportback req;
    private final int               campaignId;

    public ReportbackUploadTask(RequestReportback req, int campaignId)
    {
        this.req = req;
        this.campaignId = campaignId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        req.file = "test";

/*signup
curl -X POST \
-H "X-DS-Application-Id: 456" \
-H "X-DS-REST-API-Key: abc4324" \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-H "Session: bzBQSEFWZlJ4ZWJZWjBuSkhya2d6NkNBVEFkWWVEOG95YmdNcVF0R2YzMD0=" \
-d '{"source": "android", "group": 123}' \
https://northstar-qa.dosomething.org/v1/user/campaigns/1261/signup
*/
/*submit
curl -X POST \
-H "X-DS-Application-Id: 456" \
-H "X-DS-REST-API-Key: abc4324" \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-H "Session: bzBQSEFWZlJ4ZWJZWjBuSkhya2d6NkNBVEFkWWVEOG95YmdNcVF0R2YzMD0=" \
-d '{"quantity": "100", "why_participated": "because!", "caption": "my photo caption!", "file": "A huge base64 encoded string"}' \
https://northstar-qa.dosomething.org/v1/user/campaigns/1261/reportback
*/
        String sessionToken = AppPrefs.getInstance(context).getSessionToken();
        Response response = NetworkHelper.getNorthstarAPIService()
                .submitReportback(sessionToken, req, campaignId);
        String s = response.toString();
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }
}
