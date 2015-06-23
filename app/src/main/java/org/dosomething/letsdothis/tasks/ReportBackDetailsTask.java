package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseReportBack;
import org.dosomething.letsdothis.network.models.ResponseUser;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class ReportBackDetailsTask extends BaseNetworkErrorHandlerTask
{
    private final int        reportBackId;
    public        ReportBack reportBack;
    public        User       user;

    public ReportBackDetailsTask(int campaignId)
    {
        this.reportBackId = campaignId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseReportBack response = NetworkHelper.getDoSomethingAPIService()
                                                   .reportBack(reportBackId);
        reportBack = ResponseReportBack.getReportBack(response);


        ResponseUser responseUser = NetworkHelper.getNorthstarAPIService()
                                                   .userProfileWithDrupalId(reportBack.user.id);
        user = ResponseUser.getUser(responseUser);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
