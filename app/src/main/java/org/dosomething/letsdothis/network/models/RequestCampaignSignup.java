package org.dosomething.letsdothis.network.models;
/**
 * Created by izzyoji :) on 6/26/15.
 */
public class RequestCampaignSignup
{
    public String source = "android";
    public Integer group;

    public RequestCampaignSignup(Integer groupId)
    {
        group = groupId;
    }
}
