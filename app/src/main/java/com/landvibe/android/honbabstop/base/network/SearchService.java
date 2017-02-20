package com.landvibe.android.honbabstop.base.network;

import com.landvibe.android.honbabstop.base.domain.SearchDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

/**
 * Created by user on 2017-02-20.
 */

public interface SearchService {

    String CLIENT_ID="X-Naver-Client-Id";
    String CLIENT_SECRET="X-Naver-Client-Secret";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //https://openapi.naver.com/v1/search/local.json?
    // query=강남불백&display=10&start=1&sort=random
    @GET("v1/search/local.json")
    Call<SearchDTO> searchRestaurant(
            @Query("query") String searchQuery,
            @Query("display") int display,
            @Query("start") int start,
            @Query("sort") String sort,
            @HeaderMap Map<String, String> headers);
            /*  X-Naver-Client-Id : client id
            *   X-Naver-Client-Secret : client secret key
            * */

}
