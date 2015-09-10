package org.dosomething.letsdothis.utils;
import android.content.Context;
import android.text.format.DateUtils;

import org.dosomething.letsdothis.LDTApplication;
import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static org.dosomething.letsdothis.ui.views.typeface.CustomTypefaceSpan.format;
import static org.dosomething.letsdothis.ui.views.typeface.TypefaceManager.BRANDON_BOLD;

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
        else if(duration < DateUtils.HOUR_IN_MILLIS)
        {
            count = (int) (duration / DateUtils.MINUTE_IN_MILLIS);
            return context.getResources().getQuantityString(R.plurals.minutes_ago, count, count);
        }
        else if(duration < DateUtils.DAY_IN_MILLIS)
        {
            count = (int) (duration / DateUtils.HOUR_IN_MILLIS);
            return context.getResources().getQuantityString(R.plurals.hours_ago, count, count);
        }
        else if(duration < DateUtils.WEEK_IN_MILLIS)
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

    public static List<String> getTimeUntilExpiration(Long expireTimeInMillis)
    {
        List<String> expire = new ArrayList<>();

        long now = System.currentTimeMillis();
        long timeRemaining = expireTimeInMillis + DateUtils.HOUR_IN_MILLIS + DateUtils.MINUTE_IN_MILLIS * 3 - now;
        String days = "0";
        String hours = "0";
        String mins = "0";

        if(timeRemaining > 0)
        {
            days = Long.toString(timeRemaining / DateUtils.DAY_IN_MILLIS);
            timeRemaining %= DateUtils.DAY_IN_MILLIS;
            hours = Long.toString(timeRemaining / DateUtils.HOUR_IN_MILLIS);
            timeRemaining %= DateUtils.HOUR_IN_MILLIS;
            mins = Long.toString(timeRemaining / DateUtils.MINUTE_IN_MILLIS);
        }

        Context context = LDTApplication.getContext();
        expire.add(format(context, days, BRANDON_BOLD).toString());
        expire.add(format(context, hours, BRANDON_BOLD).toString());
        expire.add(format(context, mins, BRANDON_BOLD).toString());

        return expire;
    }

    public static long getStartOfNextMonth()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTimeInMillis();
    }

    public static boolean isCampaignExpired(Campaign campaign)
    {
        if (campaign.endTime == 0) {
            return false;
        }

        return campaign.endTime < System.currentTimeMillis();
    }
}
