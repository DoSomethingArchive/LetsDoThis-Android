package org.dosomething.letsdothis.network.models;
/**
 * Created by izzyoji :) on 4/21/15.
 */
public class ResponseImage
{
    public int nid;
    public boolean is_dark_image;
    public ResponseImageUrl url;

    public ResponseImageUrl getUrl()
    {
        return url == null ? new ResponseImageUrl() : url;
    }
}
