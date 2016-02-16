package org.dosomething.letsdothis.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jon on 2/16/2016.
 */
public class ResponseProfileCampaign {
    public String id;
    public String title;
    public CampaignRunsWrapper campaign_runs;
    public ReportbackInfoWrapper reportback_info;

    public class CampaignRunsWrapper {
        public CurrentWrapper current;

        public class CurrentWrapper {
            public IdWrapper en;

            @SerializedName("en-global")
            public IdWrapper en_global;

            // public IdWrapper past[];

            public class IdWrapper {
                public String id;
            };
        }
    }

    public class ReportbackInfoWrapper {
        public String copy;
        public String confirmation_message;
        public String noun;
        public String verb;
    }
}
