package com.utilities.shyamv.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShyamV on 8/27/2015.
 */

//new comment
    //new comment 1 changed one
public class DateTimeUtils
{
    private static String dateFormat = "EEE, MMM dd,yyyy";
    private static String timeFormat = "hh:mm aa";

    //H 	hour in day (0-23)
    //h 	hour in am/pm (1-12)
    //K 	hour in am/pm (0-11)
    //k 	hour in day (1-24)
    //m 	minute in hour
    //s 	second in minute
    //a 	am/pm marker   //AM PM
    //y 	year
    //M 	month in year  //M:1 MM:01 MMM:Jan MMMM:January MMMMM:J
    //d 	day in month
    //E 	day of week   //E/EE/EEE:Tue, EEEE:Tuesday, EEEEE:T

    public static String getDateFormat()
    {
        return dateFormat;
    }

    public static void setDateFormat(String dateFormat)
    {
        DateTimeUtils.dateFormat = dateFormat;
    }

    public static String getTimeFormat()
    {
        return timeFormat;
    }

    public static void setTimeFormat(String timeFormat)
    {
        DateTimeUtils.timeFormat = timeFormat;
    }

    public static String getDateString(int dayOfMonth, int monthOfYear, int year)
    {
        String dateStr = dayOfMonth + "-" + (monthOfYear) + "-" + year;

        return getDateTimeInFormat(dateStr, "dd-MM-yyyy", dateFormat);
    }

    public static String getTimeString(int hourOfDay, int minute)
    {
        String timeStr = hourOfDay + ":" + minute;

        return getDateTimeInFormat(timeStr, "HH:mm", timeFormat);
    }

    public static Calendar getCalendar(String inDate, String inTime)
    {
        if(inDate == null || inDate.length() == 0 || inTime == null || inTime.length() == 0)
            return null;

        /*String formatStr = "";
        String dateStr = "";
        String del = "";
        if(inDate.length() > 0)
        {
            formatStr += dateFormat;
            dateStr += inDate;
            del += " ";
        }

        if(inTime.length() > 0)
        {
            formatStr += del + timeFormat;
            dateStr += del + inTime;
        }*/

        Calendar calendar = null;

        //if(formatStr.length() > 0)
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat + " " + timeFormat);
            calendar = Calendar.getInstance();

            try
            {
                Date date = dateFormatter.parse(inDate + " " + inTime);
                calendar.setTime(date);
            }
            catch (ParseException e)
            {
                e.printStackTrace();

                return null;
            }
        }

        return calendar;
    }

    public static String getDateTimeInFormat(String inDate, String inFormat, String toFormat)
    {
        if(inFormat == toFormat)
            return inDate;

        String dateStr = inDate;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(inFormat);
        try
        {
            Date date = dateFormatter.parse(inDate);
            dateFormatter = new SimpleDateFormat(toFormat);
            dateStr = dateFormatter.format(date);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        return dateStr;
    }
}
