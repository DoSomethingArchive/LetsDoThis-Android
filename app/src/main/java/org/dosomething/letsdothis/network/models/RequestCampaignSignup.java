package org.dosomething.letsdothis.network.models;
/**
 * Created by izzyoji :) on 6/26/15.
 */
public class RequestCampaignSignup {

    public final String source = "letsdothis_android";
    public Integer group;

    public RequestCampaignSignup() {
        group = null;
    }

    public RequestCampaignSignup(Integer groupId) {
        group = groupId;
    }

}
