package org.dosomething.letsdothis.network.models;
/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseLogin
{
    public Wrapper data;

    public static class Wrapper
    {
        public String email;
        public String mobile;
        public String created_at;
        public String updated_at;
        public String _id;
        public int drupal_id;
        public String session_token;
        public String first_name;
        public String last_name;
        public String birthday;
        public String country;
        public String photo;
    }
}
