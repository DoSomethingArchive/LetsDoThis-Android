package org.dosomething.letsdothis.utils;
import android.content.Context;
import android.text.format.DateUtils;

import org.dosomething.letsdothis.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by izzyoji :) on 4/27/15.
 */
public class TimeUtils
{
    public static final String DATE_FORMAT_MONTH_DAY_YEAR = "MMM d, yyyy";

    public static String getTimeSince(Context context, long timeInMillis)
    {
        long now = System.currentTimeMillis();

        long duration = Math.abs(now - timeInMillis);

        int count;
        if(duration < DateUtils.MINUTE_IN_MILLIS)
        {
            count = (int) (duration / DateUtils.SECOND_IN_MILLIS);
            return context.getResources().getQuantityString(R.plurals.seconds_ago, count, count);
        }
        else if (duration < DateUtils.HOUR_IN_MILLIS)
        {
            count = (int) (duration / DateUtils.MINUTE_IN_MILLIS);
            return context.getResources().getQuantityString(R.plurals.minutes_ago, count, count);
        }
        else if (duration < DateUtils.DAY_IN_MILLIS)
        {
            count = (int) (duration / DateUtils.HOUR_IN_MILLIS);
            return context.getResources().getQuantityString(R.plurals.hours_ago, count, count);
        }
        else if (duration < DateUtils.WEEK_IN_MILLIS)
        {
            count = (int) (duration / DateUtils.DAY_IN_MILLIS);
            return context.getResources().getQuantityString(R.plurals.days_ago, count, count);
        }
        else
        {
            // We know that we won't be showing the time, so it is safe to pass
            // in a null context.
            return millisecondsToMonthDayYear(timeInMillis);
        }
    }

    public static String millisecondsToMonthDayYear(long millis)
    {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_MONTH_DAY_YEAR);
        return formatter.format(new Date(millis));
    }

    public static String getTimeUntilExpiration(Long expTimeInMillis)
    {
        long now = System.currentTimeMillis();
        long timeRemaining = expTimeInMillis - now;

        String HH = Long.toString(timeRemaining / DateUtils.HOUR_IN_MILLIS);
        timeRemaining %= DateUtils.HOUR_IN_MILLIS;
        String MM = Long.toString(timeRemaining / DateUtils.MINUTE_IN_MILLIS);
        timeRemaining %= DateUtils.MINUTE_IN_MILLIS;
        if (MM.length() < 2)
        {
            MM = "0" + MM;
        }
        String ss = Long.toString(timeRemaining / 1000);
        if (ss.length() < 2)
        {
            ss = "0" + ss;
        }

        return HH + ":" + MM + ":" + ss;
    }

}
