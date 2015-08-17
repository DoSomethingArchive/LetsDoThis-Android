package org.dosomething.letsdothis.network.models;
/**
 * Created by toidiu on 7/2/15.
 */
public class RequestReportback
{
    public String quantity;
    public String uid;
    public String file;
    public final String why_participated = "(why_participated not provided in reportback submissions from the Android app)";
    public String caption;
    public final String source = "android";
}
