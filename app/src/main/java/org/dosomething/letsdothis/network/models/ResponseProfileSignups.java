package org.dosomething.letsdothis.network.models;

/**
 * Created by jon on 2/16/2016.
 */
public class ResponseProfileSignups {
    public Signup data[];

    public class Signup {
        public String id;
        public CampaignRunWrapper campaign_run;
        public ResponseProfileCampaign campaign;
        public ReportbackWrapper reportback;

        public class CampaignRunWrapper {
            public String id;
            public boolean current;
        }

        public class ReportbackWrapper {
            public String id;
            public int quantity;
            public ReportbackItemsWrapper reportback_items;

            public class ReportbackItemsWrapper {
                public int total;
                public org.dosomething.letsdothis.data.ReportBack data[];
            }
        }
    }
}
