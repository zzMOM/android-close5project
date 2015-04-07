package com.teeterpal.androidteeterpal;

import junit.framework.TestCase;

/**
 * Created by weiwu on 4/2/15.
 */
public class TiemstampClassTest extends TestCase {

    public void testTimestamp() {
        String dateTime = "03-10-2015 10:30AM";
        String output = TimestampUtils.getISO8601StringFromStringDate(dateTime);
        System.out.println(output);
    }
}
