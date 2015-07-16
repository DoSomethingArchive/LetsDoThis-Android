package org.dosomething.letsdothis.network.models;
/**
 * Created by izzyoji :) on 7/9/15.
 */
public class ResponseGroup
{
    public Wrapper data;

    public class Wrapper
    {
        public int campaign_id;
        public int signup_group;
        public ResponseUser.Wrapper[] users;
    }
}
