package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.ReportBack;

/**
 * Created by toidiu on 7/9/15.
 */
public class ResponseRbData
{
    public Wrapper data;

    public static class Wrapper
    {
        private RBItems reportback_data;

        public RBItems getReportback_data()
        {
            return reportback_data;
        }
    }

    public static class RBItems
    {
        public  String id;
        private Items  reportback_items;

        public Items getReportback_items()
        {
            return reportback_items;
        }
    }

    public static class Items
    {
        private ReportBack data[];

        public ReportBack[] getData()
        {
            return data;
        }
    }

    public static ReportBack[] getRbList(ResponseRbData response)
    {
        ReportBack[] data;
        try
        {
            data = response.data.getReportback_data().getReportback_items().getData();
        }
        catch(Throwable e)
        {
            data = new ReportBack[0];
        }
        return data;
    }
}
