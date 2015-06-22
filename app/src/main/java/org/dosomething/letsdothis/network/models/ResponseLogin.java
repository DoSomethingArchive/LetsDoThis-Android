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
        public String phone;
        public String created_at;
        public String updated_at;
        public String _id;
        public String session_token;
    }


    //    email: "cooldude6",
    //    phone: "555-555-5555",
    //    created_at: "2011-11-07T20:58:34.448Z",
    //    updated_at: "2011-11-07T20:58:34.448Z",
    //    _id: "g7y9tkhB7O",
    //    session_token: "pnktnjyb996sj4p156gjtp4im"
}
