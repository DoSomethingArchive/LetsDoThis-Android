package org.dosomething.letsdothis.network.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.utils.ISO8601;

import java.text.ParseException;
import java.util.List;


/**
 * Receives a campaign response from a web service.
 * @author izzyoji
 * @author NearChaos
 */
public class ResponseCampaign
{
    public int              id;
    public String           title;
    public String           tagline;
    public String           status;
    public String           type;
    public ResponseImage    cover_image;
    public ResponseSolution solutions;
    public ResponseFacts    facts;
    public ReportBackInfo   reportback_info;
    public MobileAppTiming  mobile_app;
    public ResponseAffiliates affiliates;
	public List<ResponseAttachment> attachments;
	public List<ResponseActionGuide> action_guides;

    public static Campaign getCampaign(ResponseCampaign response)
    {
        Campaign campaign = new Campaign();
        campaign.id = response.id;
        campaign.title = response.title;
        campaign.callToAction = response.tagline;
        campaign.status = response.status;
        campaign.type = response.type;
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
        campaign.sponsorLogo = response.affiliates.getLogo();
        // Hardcode the sponsor image for campaign 5769
        // @see https://github.com/DoSomething/LetsDoThis-iOS/issues/998
        if (campaign.id == 5769) {
            campaign.sponsorLogo = "https://www.dosomething.org/sites/default/files/SponsorLogo%20NewsCorp.png";
        }
		if (response.attachments != null) {
			// Add the attachments to the campaign model
			for (ResponseAttachment attachment : response.attachments) {
				campaign.addAttachment(attachment.getTitle(), attachment.uri);
			}
		}
		if (response.action_guides != null) {
			// Add the action guides to the campaign model
			for (ResponseActionGuide guide : response.action_guides) {
				campaign.addActionGuide(guide.title, guide.subtitle,
						guide.getIntroTitle(), guide.getIntroCopy(),
						guide.getAdditionalTitle(), guide.getAdditionalCopy());
			}
		}
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

    private class ResponseSolution
    {
        public ResponseCopy copy;
        public ResponseCopy support_copy;

        public ResponseCopy getCopy()
        {
            return copy == null
                    ? new ResponseCopy()
                    : copy;
        }

        public ResponseCopy getSupportCopy()
        {
            return support_copy == null
                    ? new ResponseCopy()
                    : support_copy;
        }


    }

	private class ResponseCopy {
		public String formatted;
		public String raw;

		public String getCopy() {
			return (formatted == null) ? raw : formatted;
		}
	}

    public static class ReportBackInfo
    {
        public String copy;
        // TODO Use later? public String confirmation_message;
        public String noun;
        public String verb;
    }

	@SuppressWarnings("unused")
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

    private class ResponseAffiliates {
        public Partner[] partners;

        public String getLogo() {
            // Just defaulting to using the first sponsor
            if (partners.length > 0 && partners[0].media != null) {
                return partners[0].media.uri;
            }
            else {
                return null;
            }
        }

        private class Partner {
            public Media media;

            private class Media {
                public String uri;
            }
        }
    }


	/**
	 * Receives an attachment associated with a campaign.
	 */
	private class ResponseAttachment {
		public String title;
		public String description;
		public String uri;

		public String getTitle() {
			return (title == null) ? description : title;
		}
// Note: the following fields are available in the response but not currently used
//		public String id;
//		public String type;
	}


	/**
	 * Receives additional details for an action guide.
	 * Note that these fields can be HTML formatted.
	 */
	private class ResponseActionGuideMore {
		/** Provides title for detail. */
		public String title;

		/** Provides copy for detail. */
		public ResponseCopy copy;
	}


	/**
	 * Receives the details for an action guide.
	 */
	private class ResponseActionGuide {
		public String title;
		public String subtitle;
		public ResponseActionGuideMore intro;
		public ResponseActionGuideMore additional_text;

		public String getIntroTitle() {
			return (intro == null) ? null : intro.title;
		}

		public String getIntroCopy() {
			return (intro == null) ? null : intro.copy.getCopy();
		}

		public String getAdditionalTitle() {
			return (additional_text == null) ? null : additional_text.title;
		}

		public String getAdditionalCopy() {
			return (additional_text == null) ? null : additional_text.copy.getCopy();
		}
	}
}
