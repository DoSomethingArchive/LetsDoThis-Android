package org.dosomething.letsdothis.network.models;
/**
 * Created by toidiu on 7/8/15.
 */
public class ResponseUserCampaign
{
    public Wrapper data[];

    public static class Wrapper
    {
        public int                    drupal_id;
        public Integer                signup_group;
        public int                    signup_id;
        public ResponseReportBackData reportback_data;

        public class ResponseReportBackData {
            public String id;
            public int quantity;
            public ResponseReportBackList reportback_items;
        }
    }
}
