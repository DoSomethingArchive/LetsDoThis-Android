package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetPastUserCampaignsTask extends BaseNetworkErrorHandlerTask
{
    public List<Campaign> pastCampaignList;

    public GetPastUserCampaignsTask()
    {
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        pastCampaignList = new ArrayList<>();
        List<Integer> currCampIds = new ArrayList<>();

        List<Campaign> currCamp = DatabaseHelper.getInstance(context).getCampDao().queryForAll();
        for(Campaign camp : currCamp)
        {
            currCampIds.add(camp.id);
        }
        ResponseUserCampaign userCampaigns = NetworkHelper.getNorthstarAPIService()
                .getUserCampaigns(AppPrefs.getInstance(context).getCurrentUserId());



        String pastIds = "";
        for(ResponseUserCampaign.Wrapper campaignData : userCampaigns.data)
        {
            if(currCampIds.contains(campaignData.drupal_id))
            {
                pastIds = campaignData.drupal_id + ",";
            }
        }
        pastIds = pastIds.substring(0, pastIds.length() - 1);


        if(! pastIds.isEmpty())
        {
            ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                    .campaignListByIds(pastIds);
            pastCampaignList = ResponseCampaignList.getCampaigns(responseCampaignList);
        }
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
