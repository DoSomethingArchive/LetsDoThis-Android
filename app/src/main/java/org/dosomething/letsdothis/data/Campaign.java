package org.dosomething.letsdothis.data;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class Campaign
{
    public int         id;
    public String      title;
    public String      callToAction;
    public String      imagePath;
    public long        startTime;
    public long        endTime;
    public String      solutionCopy;
    public String      solutionSupport;
    public String      problemFact;
    public String      count;
    public UploadShare showShare;
    public List<User> group  = new ArrayList<>();
    public String noun;
    public String verb;
    public int    signupGroup;

    public enum UploadShare
    {
        UPLOADING,
        SHOW_OFF,
        SHARE
    }
}
