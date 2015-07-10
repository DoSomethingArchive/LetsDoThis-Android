package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
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
public class GetCurrentUserCampaignTask extends BaseNetworkErrorHandlerTask
{
    public List<Campaign> currentCampaignList;

    public GetCurrentUserCampaignTask()
    {
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        currentCampaignList = new ArrayList<>();
        ArrayList<Integer> doneCampaigns = new ArrayList<Integer>();
        String currentUserId = AppPrefs.getInstance(context).getCurrentUserId();
        ResponseUserCampaign userCampaigns = NetworkHelper.getNorthstarAPIService()
                .getUserCampaigns(currentUserId);

        String s = "";
        for(ResponseUserCampaign.Wrapper c : userCampaigns.data)
        {
            if(c.reportback_data != null)
            {
                doneCampaigns.add(Integer.parseInt(c.drupal_id));
            }
            s += c.drupal_id + ",";
        }


        ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                .campaignListByIds(s);
        List<Campaign> campaigns = ResponseCampaignList.getCampaigns(responseCampaignList);

        for(int i = 0; i < campaigns.size(); i++)
        {
            Campaign c = campaigns.get(i);
            if(doneCampaigns.contains(c.id))
            {
                c.showShare = Campaign.UploadShare.SHARE;
                campaigns.set(i, c);
            }

        }

        currentCampaignList.addAll(campaigns);
        //FIXME get friend group
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
