package com.example.zivug.Api;

import java.util.Calendar;

public class TimeHelper
{
    public static String getTime()
    {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        int minute = rightNow.get(Calendar.MINUTE);
        return  hour+":"+minute;

    }
}
