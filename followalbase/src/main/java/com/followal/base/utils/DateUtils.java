package com.followal.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String ddMMyyyy = "dd-MM-yyyy hh:mm a";
    public static final String hhmma = "hh:mm a";
    public static final String ddMMyyyy_HEADER = "dd-MM-yyyy";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(ddMMyyyy, Locale.US);
    public static final SimpleDateFormat DATE_FORMAT_HEADER = new SimpleDateFormat(ddMMyyyy_HEADER, Locale.US);

    // Used to convert String to date
    public static Date convertStringToDate(String strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat, Locale.US).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Used to convert Date object to string
    public static String convertDateToString(Date objDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat, Locale.US).format(objDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Used to convert Date string to string
    public static String convertDateStringToString(String strDate,
                                                   String currentFormat, String parseFormat) {
        try {
            return convertDateToString(
                    convertStringToDate(strDate, currentFormat), parseFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Used to convert milliseconds to date
    public static String millisToDate(long millis, String format) {

        return new SimpleDateFormat(format, Locale.US).format(new Date(millis));
    }

    public static Date millisToDateObject(long millis, String format) {
        return convertStringToDate(millisToDate(millis, format), format);
    }

    public static String DateStringToSeconds(String dateString, String format) {
        String timeInMilliseconds = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            Date mDate = sdf.parse(dateString);
            if (mDate != null) {
                timeInMilliseconds = (mDate.getTime() / 1000) + "";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeInMilliseconds;
    }

    public static long convertDateToMillis(String dateString, String dateFormat) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        // sdf.setTimeZone(TimeZone.getTimeZone("MST"));
        try {
            Date mDate = sdf.parse(dateString);
            if (mDate != null) {
                timeInMilliseconds = mDate.getTime();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

}
