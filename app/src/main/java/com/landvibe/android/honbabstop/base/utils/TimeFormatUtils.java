package com.landvibe.android.honbabstop.base.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by user on 2017-02-15.
 */

public class TimeFormatUtils {

    public static String getPassByTimeStr(long prevTime){
        Calendar currentTime = Calendar.getInstance();
        int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);


        Calendar preTime = Calendar.getInstance();
        preTime.setTimeInMillis(prevTime);
        int preDay = preTime.get(Calendar.DAY_OF_MONTH);
        int preHour = preTime.get(Calendar.HOUR_OF_DAY);
        int preMinute = preTime.get(Calendar.MINUTE);

        if(currentDay==preDay){
            if(currentHour==preHour){
                if(currentMinute==preMinute){
                    return "방금전";
                }else {
                    Log.d("TimeFormatUtils", String.valueOf(currentMinute-preMinute)+" 분전");
                    return String.valueOf(currentMinute-preMinute)+" 분전";
                }
            }else {
                Log.d("TimeFormatUtils", String.valueOf(currentHour-preHour)+" 시간전");
                return String.valueOf(currentHour-preHour)+" 시간전";
            }
        }else {
            return String.valueOf(currentDay-preDay)+" 일전";
        }



    }
}
