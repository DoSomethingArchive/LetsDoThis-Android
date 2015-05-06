package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetCurrentUserCampaignTask extends BaseNetworkErrorHandlerTask
{
    private final String userId;
    public List<Campaign> campaignList;


    public GetCurrentUserCampaignTask(String id)
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
        Random rand = new Random();
        for(int i = 0; i < 3; i++)
        {
            Campaign campaign = new Campaign();
            campaign.id = 15;
            campaign.title = "Dog Days of Winter";
            campaign.callToAction = "Plant flowers in your local parks.";
            campaign.imagePath = "http://staging.beta.dosomething.org/sites/default/files/MommoGrams_hero_landscape.jpg";
            campaign.count = "5 flowers planted";
            int randomNum = rand.nextInt(11);
            for (int j = 0; j <randomNum; j++)
            {
                User user = new User();
                user.avatarPath = "http://awesomeish.com/wp-content/uploads/2013/01/Michael-Jordan.jpg";
                campaign.group.add(user);
            }

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
