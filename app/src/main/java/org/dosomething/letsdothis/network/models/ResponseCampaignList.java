package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.Campaign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public class ResponseCampaignList
{
    public ResponseCampaign[] data;

    public static List<Campaign> getCampaigns(ResponseCampaignList response)
    {
        ArrayList<Campaign> campaigns = new ArrayList<>();
        if (response != null && response.data != null) {
            for (ResponseCampaign responseCampaign : response.data) {
                Campaign campaign = ResponseCampaign.getCampaign(responseCampaign);
                campaigns.add(campaign);
            }
        }

        return campaigns;
    }
}
