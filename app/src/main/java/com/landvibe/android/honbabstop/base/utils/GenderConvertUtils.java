package com.landvibe.android.honbabstop.base.utils;

import com.landvibe.android.honbabstop.GlobalApp;
import com.landvibe.android.honbabstop.R;

/**
 * Created by user on 2017-02-16.
 */

public class GenderConvertUtils {

    public final static String MALE = GlobalApp.getGlobalApplicationContext().getString(R.string.gender_male);
    public final static String FEMALE = GlobalApp.getGlobalApplicationContext().getString(R.string.gender_female);

    public static String convertGender(String gender){
        if(gender==null){
            return null;
        }

        if(gender.equals(MALE) || gender.equals("male") || gender.equals("man")){
            return MALE;
        }else {
            return FEMALE;
        }
    }
}
