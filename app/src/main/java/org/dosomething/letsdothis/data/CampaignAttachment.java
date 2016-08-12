package org.dosomething.letsdothis.data;


/**
 * Models an attachment associated with this campaign.
 * @author NearChaos
 */
public class CampaignAttachment {
	/** Provides title to display in attachment row. */
	private String title;

	/** Provides URI to access remote attachment. */
	private String uri;

	/**
	 * Instantiates a new attachment.
	 * @param title Row display title
	 * @param uri Remote access location
	 */
	CampaignAttachment(String title, String uri) {
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
