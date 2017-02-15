package com.landvibe.android.honbabstop.base.utils;

import java.util.Calendar;

/**
 * Created by user on 2017-02-15.
 */

public class TimeFormatUtils {

    public static String getPassByTimeStr(long prevTime){
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);


        Calendar preTime = Calendar.getInstance();
        preTime.setTimeInMillis(prevTime);
        int preHour = preTime.get(Calendar.HOUR_OF_DAY);
        int preMinute = preTime.get(Calendar.MINUTE);
        if(currentHour==preHour){
            if(currentHour==preMinute){
                return "방금전";
            }else {
                return String.valueOf(currentMinute-preMinute)+"분전";
            }
        }else {
            return String.valueOf(currentHour-preHour)+"시간전";
        }
    }
}
