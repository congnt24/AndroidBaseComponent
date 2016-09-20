package com.congnt.androidbasecomponent.utility;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util to calculate and convert date time
 */
public class DateTimeUtil {

    public String getCurrentTimeInString() {
        DateFormat df = new DateFormat();
        return (String) df.format("yyyy-MM-dd hh:mm:ss", new Date());
    }

    public Date getDateFromString(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return formatter.parse(s);
        } catch (ParseException e) {
            return new Date();
        }
    }
}