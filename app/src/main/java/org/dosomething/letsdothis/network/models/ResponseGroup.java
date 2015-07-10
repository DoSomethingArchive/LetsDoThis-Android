package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * Created by izzyoji :) on 7/9/15.
 */
public class ResponseGroup
{
    public Wrapper data;

    public class Wrapper
    {
        public int campaign_id;
        public User[] users;
    }
}
