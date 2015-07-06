package org.dosomething.letsdothis.network.models;
/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseUserUpdate
{
    public Wrapper data;

    private class Wrapper
    {
        public DateWrapper updated_at;

        private class DateWrapper
        {
            public String date;
        }
    }

//    {
//        "data": {
//        "updated_at": {
//            "date": "2015-05-19 19:03:21.000000",
//                    "timezone_type": 3,
//                    "timezone": "UTC"
//        }
//    }

}
