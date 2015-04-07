package com.teeterpal.androidteeterpal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Help to transfer time format between 12 hour format and 24 hour format
 */
public class DateTimeFormatHelper {
    private SimpleDateFormat h_mm_a   = new SimpleDateFormat("h:mma");
    private SimpleDateFormat hh_mm = new SimpleDateFormat("HH:mm");

    DateTimeFormatHelper(){}

    public String timeFormatHour12ToHour24String(String hour12){
        String hour24 = "";
        try {
            Date t = h_mm_a.parse(hour12);
            hour24 = hh_mm.format(t);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hour24;
    }

    public String timeFormatHour24ToHour12String(String hour24){
        String hour12 = "";
        try {
            Date t = hh_mm.parse(hour24);
            hour12 = h_mm_a.format(t).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hour12;
    }

    public String timeFormatHour24ToHour12Integer(int hour, int minute){
        String a_p = "";
        if(hour > 12){
            hour -= 12;
            a_p = "PM";
        } else {
            a_p = "AM";
        }
        String h, m;
        h = hour < 10? "0" + hour : hour + "";
        m = minute < 10? "0" + minute : minute + "";
        return new StringBuilder().append(h).append(":").append(m).append(a_p).toString();
    }

    public String timeFormatHour12ToHour124Integer(int hour, int minute, String a_p){
        if(a_p.equals("PM")){
            hour += 12;
        }
        String h, m;
        h = hour < 10? "0" + hour : hour + "";
        m = minute < 10? "0" + minute : minute + "";
        return new StringBuilder().append(h).append(":").append(m).toString();
    }

    public long dateTimeFormatToUnixTimeStamp(String dateTime) {
        long result = 0;
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mma");
        Date date = null;
        try {
            date = (Date) df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result = date.getTime() / 1000L;

        //Log.e("ConvertToTimeStamp", dateTime + " convert to " + result);
        return result;
    }

    public String unixTimeStampToDateTimeFormat(long timeStamp){
        Date date = new Date(timeStamp * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mma");
        // Get time zone from mobile phone
        df.setTimeZone(TimeZone.getDefault());
        String dateTime = df.format(date);

        //Log.e("ConvertToDateTime", timeStamp + " convert to " + dateTime);
        return dateTime;
    }
}
