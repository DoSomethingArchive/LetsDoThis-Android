package org.dosomething.letsdothis.data;

import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.List;


/**
 * Provides a business object that represents a campaign.
 * @author izzyoji
 * @author NearChaos
 */
// TODO: Possibly remove ourself as a database field collection
public class Campaign
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants

    @DatabaseField(id = true)
    public int         id;
    @DatabaseField
    public String      title;
    @DatabaseField
    public String      callToAction;
    @DatabaseField
    public String      imagePath;
    @DatabaseField
    public long        startTime;
    @DatabaseField
    public long        endTime;
    @DatabaseField
    public String      solutionCopy;
    @DatabaseField
    public String      solutionSupport;
    @DatabaseField
    public String      problemFact;
    @DatabaseField
    public String      count;
    @DatabaseField
    public UploadShare showShare;
    @DatabaseField
    public String noun;
    @DatabaseField
    public String verb;

    public String sponsorLogo;

    public String status;

    public String type;

    public boolean userIsSignedUp = false;

    public enum UploadShare {
        UPLOADING,
        SHOW_OFF,
        SHARE
    }

	/** Provides text to view campaign in web browser using app login. */
	public String magicLinkCopy;

	/** Attachments associated with this campaign, may be null if none. */
	private List<CampaignAttachment> attachments;

	/** Action guides associated with this campaign, may be null if none. */
	private List<CampaignActionGuide> actionGuides;


	/**
	 * Adds an attachment to this campaign.
	 * @param title Title to display for attachment
	 * @param uri Remote location for attachment
	 */
	public void addAttachment(String title, String uri) {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		attachments.add(new CampaignAttachment(title, uri));
	}

	/**
	 * Indicates if we have any attachments.
	 * @return Attachments present flag
	 */
	public boolean hasAnyAttachments() {
		return attachments != null;
	}

	/**
	 * Gets the attachments associated with this campaign.
	 * @return Iterable of attachments, may be empty but never null
	 */
	public Iterable<CampaignAttachment> getAttachments() {
		return (attachments == null) ? new ArrayList<CampaignAttachment>() : attachments;
	}

	/**
	 * Adds an action guide to this campaign.  Any of the fields may be null, or may contain HTML
	 * formatting.
	 * @param title Guide title
	 * @param subtitle Guide subtitle
	 * @param introTitle Title for introduction section
	 * @param introCopy Copy for introduction section
	 * @param additionalTitle Title for additional contents
	 * @param additionalCopy Copy for additional contents
	 */
	public void addActionGuide(String title, String subtitle, String introTitle, String introCopy,
   			String additionalTitle, String additionalCopy) {
		if (actionGuides == null) {
			actionGuides = new ArrayList<>();
		}
		actionGuides.add(new CampaignActionGuide(title, subtitle, introTitle, introCopy,
				additionalTitle, additionalCopy));
	}

	/**
	 * Indicates if we have any action guides.
	 * @return Action guides present flag
	 */
	public boolean hasAnyActionGuides() {
		return actionGuides != null;
	}

	/**
	 * Gets the action guides associated with this campaign.
	 * @return Iterable of action guides, may be empty but not null
	 */
	public Iterable<CampaignActionGuide> getActionGuides() {
		return (actionGuides == null) ? new ArrayList<CampaignActionGuide>() : actionGuides;
	}
}
