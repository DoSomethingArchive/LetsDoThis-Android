package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.Campaign;

import java.util.Date;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class ResponseCampaign
{
    public int nid;
    public String title;
    public String call_to_action;
    public Date high_season_start;
    public ResponseImage image_cover;
    public String solution_support;
    public String solution_copy;

    public static Campaign getCampaign(ResponseCampaign response)
    {
        Campaign campaign = new Campaign();
        campaign.id = response.nid;
        campaign.title = response.title;
        campaign.callToAction = response.call_to_action;
        campaign.startTime = response.getHighSeasonStart();
        campaign.imagePath = response.getImageCover().getUrl().getLandscape().getRaw();
        campaign.solutionCopy = response.solution_copy;
        campaign.solutionSupport = response.solution_support;

        return campaign;
    }

    private long getHighSeasonStart()
    {
        return high_season_start == null? 0 : high_season_start.getTime();
    }

    public ResponseImage getImageCover()
    {
        return image_cover == null ? new ResponseImage() : image_cover;
    }
}
