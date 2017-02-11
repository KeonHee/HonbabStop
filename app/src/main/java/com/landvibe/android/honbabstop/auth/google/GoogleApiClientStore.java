package com.landvibe.android.honbabstop.auth.google;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by user on 2017-02-11.
 */

public class GoogleApiClientStore {

    private static GoogleApiClientStore instance=null;

    private static GoogleApiClient mGoogleApiClient=null;

    private GoogleApiClientStore(){}

    public static GoogleApiClientStore storeGoogleApiClient(GoogleApiClient googleApiClient){
        if(instance==null){
            instance=new GoogleApiClientStore();
            if(mGoogleApiClient==null){
                mGoogleApiClient=googleApiClient;
            }
        }
        return instance;
    }

    public static GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }
}
