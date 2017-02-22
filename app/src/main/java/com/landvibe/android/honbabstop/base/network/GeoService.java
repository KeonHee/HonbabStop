package com.landvibe.android.honbabstop.base.network;

import com.landvibe.android.honbabstop.base.domain.TranscoordDTO;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 2017-02-21.
 */

public interface GeoService {

    String BASE_URL="https://apis.daum.net/local/geo/transcoord";

    String API_KEY="271b8cbe67b781519aebc44b05a99c0b";

    String FROMCOORD="KTM";
    String TOCOORD="WGS84";

    String OUTPUT="json";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://apis.daum.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /*
    https://apis.daum.net/local/geo/transcoord?
    apikey={apikey}&fromCoord=WTM&y=-4388.879299157299&x=160710.37729270622&toCoord=WGS84&output=json
     */
    @GET("local/geo/transcoord")
    Call<TranscoordDTO> transCoord(
            @Query("apikey") String apiKey,
            @Query("fromCoord") String fromCoord,
            @Query("toCoord") String toCoord,
            @Query("y") long y,
            @Query("x") long x,
            @Query("output") String output
    );
}
