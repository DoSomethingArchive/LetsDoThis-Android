package org.dosomething.letsdothis.network.models;
/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseRegister
{
    public Wrapper data;

    public static class Wrapper
    {
        public String  _id;
        public String  email;
        public String  mobile;
        public String  birthday;
        public String  first_name;
        public String  last_name;
        public Integer drupal_id;
        public String  session_token;
        public String  updated_at;
        public String  created_at;
        //    created_at: 2000-01-01T00:00:00Z,
        //    _id: some sort of hash value
    }
}
