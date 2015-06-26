package org.dosomething.letsdothis.network.models;
/**
 * Created by izzyoji :) on 6/26/15.
 */
public class ResponseCampaignSignUp
{
    public Wrapper data;

    public static int getSignUpId(ResponseCampaignSignUp response)
    {
        return response.data.signup_id;
    }

    private class Wrapper
    {
        public int signup_id;
    }
}
