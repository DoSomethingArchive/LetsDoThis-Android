package org.dosomething.letsdothis.data;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by izzyoji :) on 4/17/15.
 */
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
}
