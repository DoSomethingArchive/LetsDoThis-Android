package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 5/6/15.
 */
public class CampaignGroupDetailsTask extends BaseNetworkErrorHandlerTask
{
    private final int      campaignId;
    private final String   userId;
    public        Campaign campaign;

    public CampaignGroupDetailsTask(int campaignId, String userId)
    {

        this.campaignId = campaignId;
        this.userId = userId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        //FIXME: add real API call
        campaign = new Campaign();
        campaign.id = 15;
        campaign.title = "Music March Out";
        campaign.callToAction = "Run a musical walk out to defend music \n" + "program funding at school.";
        campaign.imagePath = "http://staging.beta.dosomething.org/sites/default/files/MommoGrams_hero_landscape.jpg";


        for(int i = 0; i < 10; i++)
        {
            User user = new User();
            user.avatarPath = "http://awesomeish.com/wp-content/uploads/2013/01/Michael-Jordan.jpg";
            campaign.group.add(user);
        }

    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }
}
