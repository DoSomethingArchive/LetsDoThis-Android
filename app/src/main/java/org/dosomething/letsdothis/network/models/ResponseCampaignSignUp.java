package org.dosomething.letsdothis.network.models;

/**
 * Data structure of the response to a signup submission.
 *
 * Created by izzyoji :) on 6/26/15.
 */
public class ResponseCampaignSignUp {
    public Wrapper data;

    public static int getSignUpId(ResponseCampaignSignUp response) {
        return response.data.id;
    }

    private class Wrapper {
        int id;
        CampaignRunWrapper campaign_run;
        CampaignWrapper campaign;

        private class CampaignRunWrapper {
            String id;
            boolean current;
        }

        private class CampaignWrapper {
            String id;
            String title;
            String tagline;
        }
    }
}
