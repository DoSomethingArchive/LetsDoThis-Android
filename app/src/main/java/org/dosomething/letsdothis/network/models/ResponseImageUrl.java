package org.dosomething.letsdothis.network.models;
/**
 * Created by izzyoji :) on 4/21/15.
 */
public class ResponseImageUrl
{
    public ResponseImageUrlShape landscape;

    public ResponseImageUrlShape getLandscape()
    {
        return landscape == null ? new ResponseImageUrlShape() : landscape;
    }

}
