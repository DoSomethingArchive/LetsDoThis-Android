package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetPastUserCampaignTask extends BaseNetworkErrorHandlerTask
{
    public List<Campaign> pastCampaignList;

    public GetPastUserCampaignTask()
    {
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        //FIXME this is a fake call


        makeFakeCall(context);
    }

    private void makeFakeCall(Context context) throws NetworkException
    {
        pastCampaignList = new ArrayList<>();
        String currentUserId = AppPrefs.getInstance(context).getCurrentUserId();
        ResponseUserCampaign userCampaigns = NetworkHelper.getNorthstarAPIService()
                .getUserCampaigns(currentUserId);
        String s = "";
        for(ResponseUserCampaign.Wrapper c : userCampaigns.data)
        {
            s += c.drupal_id + ",";
        }

        ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                .campaignListByIds(s);
        List<Campaign> campaigns = ResponseCampaignList.getCampaigns(responseCampaignList);

        pastCampaignList.addAll(campaigns);
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
