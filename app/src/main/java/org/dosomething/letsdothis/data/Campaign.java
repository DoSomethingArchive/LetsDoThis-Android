package org.dosomething.letsdothis.data;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class Campaign
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final String INTEREST_GROUP = "interest_group";

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
    @DatabaseField(columnName = INTEREST_GROUP)
    public int    interestGroup;

    public boolean userIsSignedUp = false;

    public UserReportBack userReportBack;

    public enum UploadShare {
        UPLOADING,
        SHOW_OFF,
        SHARE
    }
}
