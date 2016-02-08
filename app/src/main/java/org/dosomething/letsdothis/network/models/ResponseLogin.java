package org.dosomething.letsdothis.network.models;
/**
 * Data structure of a login request's response object.
 *
 * Created by toidiu on 4/16/15.
 */
public class ResponseLogin {
    public Wrapper data;

    public static class Wrapper {
        public String key;
        public UserWrapper user;

        public class UserWrapper {
            public DataWrapper data;

            public class DataWrapper {
                public String email;
                public String mobile;
                public String id;
                public int drupal_id;
                public String first_name;
                public String last_name;
                public String birthday;
                public String country;
                public String photo;
            }
        }
    }
}
