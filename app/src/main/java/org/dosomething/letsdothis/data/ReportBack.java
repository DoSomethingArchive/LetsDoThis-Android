package org.dosomething.letsdothis.data;
import com.google.gson.annotations.SerializedName;

/**
 * Created by izzyoji :) on 4/23/15.
 */
public class ReportBack
{
    public int      id;
    public Campaign campaign;
    @SerializedName("created_at")
    public long     createdAt;
    public String   caption;
    public Media      media;
    public String   imagePath;

    public String getImagePath()
    {
        return media.uri;
    }

    private static class Media
    {
        public String uri;
        public String type;
    }
}
