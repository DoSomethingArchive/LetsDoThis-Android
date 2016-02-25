package org.dosomething.letsdothis.network.models;
/**
 * Payload of a reportback submission.
 *
 * Created by toidiu on 7/2/15.
 */
public class RequestReportback {
    // Campaign ID the reportback is being submitted for
    public int campaign_id;

    // Reportback quantity
    public String quantity;

    // Reportback image base64 encoded
    public String file;

    // Reportback caption
    public String caption;

    // Required by the API, but not a field we're allowing users to fill out here
    public final String why_participated = "(why_participated not provided in reportback submissions from the Android app)";

    // Reportback source
    public final String source = "mobileapp_android";
}
