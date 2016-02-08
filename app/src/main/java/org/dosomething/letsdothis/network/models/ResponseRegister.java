package org.dosomething.letsdothis.network.models;
/**
 * Data structure of a register request's response object.
 *
 * Created by toidiu on 4/16/15.
 */
public class ResponseRegister {
    public Wrapper data;

    public static class Wrapper {
        public String key;
        public UserWrapper user;

        public class UserWrapper {
            public DataWrapper data;

            public class DataWrapper {
                public String id;
                public String email;
                public String mobile;
                public String birthday;
                public String first_name;
                public String last_name;
                public Integer drupal_id;
            }
        }
    }
}
