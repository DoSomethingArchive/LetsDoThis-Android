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
    public Date high_season_end;
    public ResponseImage image_cover;
    public String solution_support;
    public String solution_copy;
    public ResponseFactProblem fact_problem;

    public static Campaign getCampaign(ResponseCampaign response)
    {
        Campaign campaign = new Campaign();
        campaign.id = response.nid;
        campaign.title = response.title;
        campaign.callToAction = response.call_to_action;
        campaign.startTime = response.getHighSeasonStart();
        campaign.endTime = response.getHighSeasonEnd();
        campaign.imagePath = response.getImageCover().getUrl().getLandscape().getRaw();
        campaign.solutionCopy = response.solution_copy;
        campaign.solutionSupport = response.solution_support;
        campaign.problemFact = response.getFactProblem().getFact();

        return campaign;
    }

    private ResponseFactProblem getFactProblem()
    {
        return fact_problem == null ? new ResponseFactProblem() : fact_problem;
    }

    private long getHighSeasonStart()
    {
        return high_season_start == null? 0 : high_season_start.getTime();
    }

    private long getHighSeasonEnd()
    {
        return high_season_end == null? 0 : high_season_end.getTime();
    }

    public ResponseImage getImageCover()
    {
        return image_cover == null ? new ResponseImage() : image_cover;
    }

    private static class ResponseImage
    {
        public ResponseImageUrl url;

        public ResponseImageUrl getUrl()
        {
            return url == null
                    ? new ResponseImageUrl()
                    : url;
        }

        private static class ResponseImageUrl
        {
            public ResponseImageUrlShape landscape;

            public ResponseImageUrlShape getLandscape()
            {
                return landscape == null
                        ? new ResponseImageUrlShape()
                        : landscape;
            }

        }

        private static class ResponseImageUrlShape
        {
            public String raw;

            public String getRaw()
            {
                return raw == null
                        ? ""
                        : raw;
            }
        }
    }

    private static class ResponseFactProblem
    {
        public String fact;

        public String getFact()
        {
            return fact == null ? "" : fact;
        }
    }
}
