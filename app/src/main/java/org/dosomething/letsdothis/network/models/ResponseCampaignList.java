package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.Campaign;

import java.util.ArrayList;
import java.util.List;

/**
 * Response from request for a list of campaigns.
 *
 * Created by izzyoji :) on 6/23/15.
 */
public class ResponseCampaignList {

    private PaginationWrapper pagination;
    private ResponseCampaign[] data;

    private class PaginationWrapper {
        int current_page;
        int total_pages;
    }

    /**
     * Returns the current page number of the response.
     *
     * @return int
     */
    public int getCurrentPage() {
        return pagination != null ? pagination.current_page : 0;
    }

    /**
     * Returns the total number of pages available.
     *
     * @return int
     */
    public int getTotalPages() {
        return pagination != null ? pagination.total_pages : 0;
    }

    /**
     * Transform the data array into a usable list of Campaigns.
     *
     * @param hideClosed Exclude closed campaigns in the return
     * @return List<Campaign>
     */
    public List<Campaign> getCampaigns(boolean hideClosed) {
        ArrayList<Campaign> campaigns = new ArrayList<>();
        if (data != null) {
            for (ResponseCampaign responseCampaign : data) {
                Campaign campaign = ResponseCampaign.getCampaign(responseCampaign);

                if (campaign != null) {
                    boolean okToAdd = true;
                    // Only keep campaigns where type = "campaign"
                    okToAdd = campaign.type != null && campaign.type.equals("campaign");

                    // Filter out closed campaigns by checking for status = "active"
                    if (hideClosed) {
                        okToAdd = campaign.status != null && campaign.status.equals("active");
                    }

                    if (okToAdd) {
                        campaigns.add(campaign);
                    }
                }
            }
        }

        return campaigns;
    }
}
