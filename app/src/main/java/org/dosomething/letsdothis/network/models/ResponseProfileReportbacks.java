package org.dosomething.letsdothis.network.models;

/**
 * Created by jon on 2/16/2016.
 */
public class ResponseProfileReportbacks {
    public Reportback data[];

    public class Reportback {
        public String id;
        public int quantity;
        public String why_participated;
        public ReportbackItemsWrapper reportback_items;
        public ResponseProfileCampaign campaign;
    }

    public class ReportbackItemsWrapper {
        public int total;
        public org.dosomething.letsdothis.data.ReportBack data[];
    }
}
