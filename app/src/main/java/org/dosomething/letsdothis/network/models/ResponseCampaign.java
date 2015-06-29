package org.dosomething.letsdothis.network.models;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.utils.ISO8601;

import java.text.ParseException;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class ResponseCampaign
{
    public int              id;
    public String           title;
    public String           tagline;
    public ResponseImage    cover_image;
    public ResponseSolution solutions;
    public ResponseFacts    facts;
    public ReportBackInfo   reportback_info;
    public ReportTiming     timing;


    public static Campaign getCampaign(ResponseCampaign response)
    {
        Campaign campaign = new Campaign();
        campaign.id = response.id;
        campaign.title = response.title;
        campaign.callToAction = response.tagline;
        campaign.startTime = getMillisFromString(response.getTiming().getHighSeason().start);
        campaign.endTime = getMillisFromString(response.getTiming().getHighSeason().end);
        campaign.imagePath = response.getCoverImage().getWrapper().getSizes().getLandscape()
                                     .getUri();
        campaign.solutionCopy = response.getSolutions().getCopy().formatted;
        campaign.solutionSupport = response.getSolutions().getCopy().formatted;
        campaign.problemFact = response.getFacts().getProblem();
        campaign.count = "";//response.getCountString();

        return campaign;
    }

    private static long getMillisFromString(String dateString)
    {
        try
        {
            return TextUtils.isEmpty(dateString)
                    ? 0
                    : ISO8601.toTimeInMillis(dateString);
        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    private ReportTiming getTiming()
    {
        return timing == null
                ? new ReportTiming()
                : timing;
    }


    public ResponseSolution getSolutions()
    {
        return solutions == null
                ? new ResponseSolution()
                : solutions;
    }

    private ResponseFacts getFacts()
    {
        return facts == null
                ? new ResponseFacts()
                : facts;
    }

    public ResponseImage getCoverImage()
    {
        return cover_image == null
                ? new ResponseImage()
                : cover_image;
    }

    private static class ResponseImage
    {
        @SerializedName("default")
        public ResponseImageWrapper wrapper;

        public ResponseImageWrapper getWrapper()
        {
            return wrapper == null
                    ? new ResponseImageWrapper()
                    : wrapper;
        }

        private static class ResponseImageWrapper
        {
            public ResponseImageWrapperSize sizes;

            public ResponseImageWrapperSize getSizes()
            {
                return sizes == null
                        ? new ResponseImageWrapperSize()
                        : sizes;
            }

        }

        private static class ResponseImageWrapperSize
        {
            public ResponseImageWrapperSizeWrapper landscape;

            public ResponseImageWrapperSizeWrapper getLandscape()
            {
                return landscape == null
                        ? new ResponseImageWrapperSizeWrapper()
                        : landscape;
            }

            private class ResponseImageWrapperSizeWrapper
            {
                public String uri;

                public String getUri()
                {
                    return TextUtils.isEmpty(uri)
                            ? ""
                            : uri;
                }
            }
        }
    }

    private static class ResponseFacts
    {
        public String problem;

        public String getProblem()
        {
            return problem == null
                    ? ""
                    : problem;
        }
    }

    private static class Stats
    {
        public int signups;
    }

    private class ResponseSolution
    {
        public ResponseSolutionObject copy;
        public ResponseSolutionObject support_copy;

        public ResponseSolutionObject getCopy()
        {
            return copy == null
                    ? new ResponseSolutionObject()
                    : copy;
        }

        public ResponseSolutionObject getSupportCopy()
        {
            return support_copy == null
                    ? new ResponseSolutionObject()
                    : support_copy;
        }


        private class ResponseSolutionObject
        {
            public String formatted;
            public String raw;
        }
    }

    private class ReportBackInfo
    {
        public String copy;
        public String confirmation_message;
        public String noun;
        public String verb;
    }

    public ReportBackInfo getReportbackInfo()
    {
        return reportback_info == null
                ? new ReportBackInfo()
                : reportback_info;
    }

    private class ReportTiming
    {
        public ResponseSeason high_season;

        public ResponseSeason getHighSeason()
        {
            return high_season == null
                    ? new ResponseSeason()
                    : high_season;
        }

        private class ResponseSeason
        {
            public String start;
            public String end;
        }
    }
}
