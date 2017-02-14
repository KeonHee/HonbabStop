package com.landvibe.android.honbabstop;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.kakao.auth.KakaoSDK;
import com.landvibe.android.honbabstop.base.auth.kakao.KakaoSDKAdapter;

/**
 * Created by user on 2017-02-07.
 */

public class GlobalApp extends MultiDexApplication {

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


    /**
     * Glide 이미지 캐시로인한 메모리 부족현상 컨트롤
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }
}
