package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.ReportBack;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class ResponseReportBack
{
    public ReportBack data;

    public static ReportBack getReportBack(ResponseReportBack response)
    {
        return response.data;
    }
}
