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

	private List<Attachment> attachments;


	/**
	 * Adds an attachment to this campaign.
	 * @param title Title to display for attachment
	 * @param uri Remote location for attachment
	 */
	public void addAttachment(String title, String uri) {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		attachments.add(new Attachment(title, uri));
	}


	/**
	 * Indicates if we have any attachments.
	 * @return Attachments present flag
	 */
	public boolean isAnyAttachments() {
		return attachments != null;
	}


	/**
	 * Gets the attachments associated with this campaign.
	 * @return Iterable attachments list, may be empty but never null
	 */
	public Iterable<Attachment> getAttachments() {
		return (attachments == null) ? new ArrayList<Attachment>() : attachments;
	}


	/**
	 * Indicates if we have any action guides.
	 * @return
	 */
	public boolean isAnyActionGuides() {
		// TODO Real implementation
		return false;
	}


	/**
	 * Models an attachment associated with this campaign.
	 */
	public class Attachment {
		/** Provides title to display in attachment row. */
		private String title;

		/** Provides URI to access remote attachment. */
		private String uri;


		/**
		 * Instantiates a new attachment.
		 * @param title Row display title
		 * @param uri Remote access location
		 */
		Attachment(String title, String uri) {
			this.title = title;
			this.uri = uri;
		}


		/**
		 * Gets the title to display for the attachment.
		 * @return Attachment title
		 */
		public String getTitle() {
			return title;
		}


		/**
		 * Gets the URI to access the remote attachment.
		 * @return Remote URI
		 */
		public String getUri() {
			return uri;
		}
	}
}
