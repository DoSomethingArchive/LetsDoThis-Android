package org.dosomething.letsdothis.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.w3c.dom.Text;


/**
 * Models an action guide associated with this campaign.
 * @author NearChaos
 */
public class CampaignActionGuide implements Parcelable {
	/** Provides the title for this guide. */
	private String title;

	/** Provides the subtitle for this guide. */
	private String subtitle;

	/** Provides the title for the intro text to this guide. */
	private String introTitle;

	/** Provides the copy for the intro text to this guide, may have HTML formatting. */
	private String introCopy;

	/** Provides the title for the additional text to this guide. */
	private String additionalTitle;

	/** Provides the copy for the additional text to this guide, may have HTML formatting. */
	private String additionalCopy;

	/**
	 * Instantiates a new action guide.  Any of the fields may be null, or may contain HTML
	 * formatting.
	 * @param title Guide title
	 * @param subtitle Guide subtitle
	 * @param introTitle Title for introduction section
	 * @param introCopy Copy for introduction section
	 * @param additionalTitle Title for additional contents
	 * @param additionalCopy Copy for additional contents
	 */
	CampaignActionGuide(String title, String subtitle, String introTitle, String introCopy,
				String additionalTitle, String additionalCopy) {
		this.title = title;
		this.subtitle = subtitle;
		this.introTitle = introTitle;
		this.introCopy = introCopy;
		this.additionalTitle = additionalTitle;
		this.additionalCopy = additionalCopy;
	}

	/**
	 * Instantiates by deserializing parcel.
	 * @param source Source parcel to deserialize
	 */
	private CampaignActionGuide(Parcel source) {
		this.title = unpack(source);
		this.subtitle = unpack(source);
		this.introTitle = unpack(source);
		this.introCopy = unpack(source);
		this.additionalTitle = unpack(source);
		this.additionalCopy = unpack(source);
	}

	/**
	 * Gets the title for this guide.
	 * @return Guide title or null
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Indicates if this guide has a title.
	 * @return Has title flag
	 */
	public boolean hasTitle() {
		return !TextUtils.isEmpty(title);
	}

	/**
	 * Gets the subtitle for this guide.
	 * @return Guide subtitle or null
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * Indicates if this guide has a subtitle.
	 * @return Has subtitle flag
	 */
	public boolean hasSubtitle() {
		return !TextUtils.isEmpty(subtitle);
	}

	/**
	 * Gets the title to the introduction section of this guide.
	 * @return Introduction title or null
	 */
	public String getIntroTitle() {
		return introTitle;
	}

	/**
	 * Indicates if this guide has an introduction section title.
	 * @return Has introduction title
	 */
	public boolean hasIntroTitle() {
		return !TextUtils.isEmpty(introTitle);
	}

	/**
	 * Gets the copy for the introduction section of this guide.
	 * @return Introduction copy or null
	 */
	public String getIntroCopy() {
		return introCopy;
	}

	/**
	 * Indicates if this guide has introduction section copy.
	 * @return Has introduction copy
	 */
	public boolean hasIntroCopy() {
		return !TextUtils.isEmpty(introCopy);
	}

	/**
	 * Gets the title for the additional section of this guide.
	 * @return Additional section title or null
	 */
	public String getAdditionalTitle() {
		return additionalTitle;
	}

	/**
	 * Indicates if this guide has a title for its additional section.
	 * @return Has additional title
	 */
	public boolean hasAdditionalTitle() {
		return !TextUtils.isEmpty(additionalTitle);
	}

	/**
	 * Gets the copy for the additional section of this guide.
	 * @return Additional section copy or null
	 */
	public String getAdditionalCopy() {
		return additionalCopy;
	}

	/**
	 * Indicates if this guide has additional copy.
	 * @return Has additional copy flag
	 */
	public boolean hasAdditionalCopy() {
		return !TextUtils.isEmpty(additionalCopy);
	}

	/**
	 * Describe the kinds of special objects contained in this Parcelable's
	 * marshalled representation.
	 *
	 * @return a bitmask indicating the set of special object types marshalled
	 * by the Parcelable.
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Flatten this object in to a Parcel.
	 *
	 * @param dest  The Parcel in which the object should be written.
	 * @param flags Additional flags about how the object should be written.
	 *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		pack(dest, title);
		pack(dest, subtitle);
		pack(dest, introTitle);
		pack(dest, introCopy);
		pack(dest, additionalTitle);
		pack(dest, additionalCopy);
	}

	/**
	 * Required class factory to deserialize from parcel.
	 */
	public static final Parcelable.Creator<CampaignActionGuide> CREATOR
			= new Parcelable.Creator<CampaignActionGuide>() {
		public CampaignActionGuide createFromParcel(Parcel in) {
			return new CampaignActionGuide(in);
		}

		public CampaignActionGuide[] newArray(int size) {
			return new CampaignActionGuide[size];
		}
	};

	/**
	 * Packs a possibly null string to a parcel.
	 * @param dest Parcel receives string
	 * @param source Possibly null string
	 */
	private void pack(Parcel dest, String source) {
		if (source == null) {
			dest.writeByte((byte) 1);
		} else {
			dest.writeByte((byte) 0);
			dest.writeString(source);
		}
	}

	/**
	 * Unpacks a possibly null string from a parcel.
	 * @param source Parcel to unpack
	 * @return String or null
	 */
	private String unpack(Parcel source) {
		if (source.readByte() == 0) {
			return source.readString();
		}
		return null;
	}
}
