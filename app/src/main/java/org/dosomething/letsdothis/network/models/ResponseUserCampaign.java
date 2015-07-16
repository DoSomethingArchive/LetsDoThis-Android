package org.dosomething.letsdothis.network.models;
/**
 * Created by toidiu on 7/8/15.
 */
public class ResponseUserCampaign
{
    public Wrapper data[];

    public static class Wrapper
    {
        public String drupal_id;
        public ResponseReportBackData reportback_data;

        public class ResponseReportBackData
        {
            public ResponseReportBackList reportback_items;
        }
    }
}
