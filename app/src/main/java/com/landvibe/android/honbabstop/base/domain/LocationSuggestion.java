package com.landvibe.android.honbabstop.base.domain;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by user on 2017-02-20.
 */

public class LocationSuggestion implements SearchSuggestion {

    private String mLocationName;


    public LocationSuggestion(String suggestion){
        this.mLocationName=suggestion;
    }

    public LocationSuggestion(Parcel source){
        this.mLocationName=source.readString();
    }

    @Override
    public String getBody() {
        return mLocationName;
    }


    public static final Creator<LocationSuggestion> CREATOR = new Creator<LocationSuggestion>() {
        @Override
        public LocationSuggestion createFromParcel(Parcel in) {
            return new LocationSuggestion(in);
        }

        @Override
        public LocationSuggestion[] newArray(int size) {
            return new LocationSuggestion[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLocationName);
    }
}
