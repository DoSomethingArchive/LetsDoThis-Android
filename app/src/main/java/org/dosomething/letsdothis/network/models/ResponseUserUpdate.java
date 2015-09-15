package org.dosomething.letsdothis.network.models;

import org.dosomething.letsdothis.data.User;

/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseUserUpdate {
    public Wrapper data;

    private class Wrapper {
        public User user;
    }
}
