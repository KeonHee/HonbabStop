package com.landvibe.android.honbabstop.base.utils;

/**
 * Created by user on 2017-02-15.
 */

public class CheckUtils {

    public static boolean isFirebaseStorage(String url){
        return url.startsWith("gs://");
    }
}
