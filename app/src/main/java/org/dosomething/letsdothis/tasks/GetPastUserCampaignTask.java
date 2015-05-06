package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetPastUserCampaignTask extends BaseNetworkErrorHandlerTask
{
    private final String userId;
    public List<Campaign> campaignList;


    public GetPastUserCampaignTask(String id)
    {
        this.userId = id;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        campaignList = new ArrayList<>();
        // FIXME eventually hook up the api to get user campaigns

        makeFakeCurrentData();
    }

    private void makeFakeCurrentData()
    {
        for(int i = 0; i < 3; i++)
        {
            Campaign campaign = new Campaign();
            campaign.title = "Teens for Jeans";
            campaign.imagePath = "http://staging.beta.dosomething.org/sites/default/files/images/Vamps_landscape_1.jpg";

            campaignList.add(campaign);
        }
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
