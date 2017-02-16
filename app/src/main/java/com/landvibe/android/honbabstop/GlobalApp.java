package com.landvibe.android.honbabstop;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.kakao.auth.KakaoSDK;
import com.landvibe.android.honbabstop.base.auth.kakao.KakaoSDKAdapter;
import com.landvibe.android.honbabstop.base.observer.CustomObserver;
import com.landvibe.android.honbabstop.base.observer.Observer;

import java.util.ArrayList;

/**
 * Created by user on 2017-02-07.
 */

public class GlobalApp extends MultiDexApplication implements Observer {

    private final static String TAG = "GlobalApp";

    private static GlobalApp mInstance;

    private ArrayList<CustomObserver> observers;
    private Object obj;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mInstance=this;
        observers=new ArrayList<>();
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

    /**
     * 효율적인 데이터 갱신을 위한 옵저버 등록
     */
    @Override
    public void addObserver(CustomObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(CustomObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        synchronized (obj) {
            for (CustomObserver observer : observers) {
                if (observer != null) {
                    observer.update(obj);
                }
            }
        }
    }

    public  void changeModel(Object model) {
        this.obj = model;
        notifyObservers();
    }
}
