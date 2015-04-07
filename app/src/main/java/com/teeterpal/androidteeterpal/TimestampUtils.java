package com.teeterpal.androidteeterpal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by weiwu on 4/2/15.
 */
public class TimestampUtils {



    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getISO8601StringFromStringDate(String dateTime) {
        if(dateTime.equals("")){
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mma");
        Date date = null;
        try {
            date = (Date) df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date d = new Date(date.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(d);
    }

    public static String getISO8601StringFromDate(Date date) {
        if(date == null){
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static Date getDateFromString(String dateTime) {
        if(dateTime.equals("")){
            return null;
        }
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy hh:mma");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        try {
            date = (Date) inputFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getStringFromDate(Date date) {
        if(date == null){
            return "";
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy hh:mma");
        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFormat.format(date);
    }
}
