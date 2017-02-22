package com.landvibe.android.honbabstop.base.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2017-02-21.
 */

public class TranscoordDTO {

    @SerializedName("y")
    double lat;

    @SerializedName("x")
    double lon;

    public TranscoordDTO() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
