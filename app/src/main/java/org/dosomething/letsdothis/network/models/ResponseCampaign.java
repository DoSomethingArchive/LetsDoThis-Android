package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.Campaign;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class ResponseCampaign
{
    public String title;
    public boolean is_staff_pick;

    public static Campaign getCampaign(ResponseCampaign response)
    {
        Campaign campaign = new Campaign();
        campaign.title = response.title;
        campaign.staffPick = response.is_staff_pick;

        return campaign;
    }
}
