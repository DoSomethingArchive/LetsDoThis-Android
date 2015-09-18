package org.dosomething.letsdothis.network.models;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.utils.ISO8601;

import java.text.ParseException;
import java.util.ArrayList;

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
    public MobileAppTiming  mobile_app;

    public static Campaign getCampaign(ResponseCampaign response)
    {
        Campaign campaign = new Campaign();
        campaign.id = response.id;
        campaign.title = response.title;
        campaign.callToAction = response.tagline;
        campaign.startTime = getMillisFromString(response.getMobileAppTiming().getDates().start);
        campaign.endTime = getMillisFromString(response.getMobileAppTiming().getDates().end);
        campaign.imagePath = response.getCoverImage().getWrapper().getSizes().getLandscape()
                .getUri();
        campaign.solutionCopy = response.getSolutions().getCopy().raw;
        campaign.solutionSupport = response.getSolutions().getSupportCopy().raw;
        campaign.problemFact = response.getFacts().getProblem();
        campaign.count = "";//response.getCountString();
        campaign.showShare = Campaign.UploadShare.SHOW_OFF;
        campaign.noun = response.reportback_info.noun;
        campaign.verb = response.reportback_info.verb;
        campaign.group = new ArrayList<>();
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

    private MobileAppTiming getMobileAppTiming() {
        return mobile_app == null
                ? new MobileAppTiming()
                : mobile_app;
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

    public static class ReportBackInfo
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

    private class MobileAppTiming {
        public MobileAppDates dates;

        public MobileAppDates getDates() {
            return dates == null
                    ? new MobileAppDates()
                    : dates;
        }

        private class MobileAppDates {
            public String start;
            public String end;
        }
    }
}
