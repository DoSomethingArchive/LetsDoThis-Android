package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.ReportBack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by izzyoji :) on 4/23/15.
 */
public class ResponseReportBackList
{

    public ReportBack data[];
    public Pagination pagination;

    public static List<ReportBack> getReportBacks(ResponseReportBackList response)
    {
        //FIXME crashlytics
        try
        {
            return new ArrayList<>(Arrays.asList(response.data));
        }
        catch(NullPointerException e)
        {
            return new ArrayList<>();
        }
    }

    public static class Pagination
    {
        public int total;
        public int per_page;
        public int current_page;
        public int total_pages;
    }
}
