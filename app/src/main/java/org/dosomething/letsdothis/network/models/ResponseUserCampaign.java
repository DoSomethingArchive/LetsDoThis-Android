package org.dosomething.letsdothis.network.models;
import java.util.Objects;

/**
 * Created by toidiu on 7/8/15.
 */
public class ResponseUserCampaign
{
    public Wrapper data[];

    public static class Wrapper
    {
        public String drupal_id;
        //                "signup_id": 1939,
        //                "signup_source": "android",
        //                "updated_at": "2015-07-02 20:43:36",
        //                "created_at": "2015-06-29 15:24:23",
//        public int    reportback_id;
        public Object reportback_data;
    }
}
