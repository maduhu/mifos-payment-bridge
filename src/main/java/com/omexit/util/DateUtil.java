package com.omexit.util;

import org.joda.time.DateTime;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aomeri on 1/5/2016.
 */
public class DateUtil {

    public final static String DEFAULT_DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDate(String strDate, String format)  {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date=null;
        try {
            date= sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int checkAge(DateTime dateOfBirth) {
        return Years.yearsBetween(dateOfBirth, new DateTime()).getYears();
    }
}
