package com.landvibe.android.honbabstop;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.kakao.auth.KakaoSDK;
import com.landvibe.android.honbabstop.auth.kakao.KakaoSDKAdapter;

/**
 * Created by user on 2017-02-07.
 */

public class GlobalApp extends Application {

    private final static String TAG = "GlobalApp";

    private static GlobalApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mInstance=this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG, "attachBaseContext");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;
    }

    public static GlobalApp getGlobalApplicationContext(){
        if(mInstance==null){
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        }
        return mInstance;
    }


}
