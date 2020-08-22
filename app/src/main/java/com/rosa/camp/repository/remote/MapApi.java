package com.rosa.camp.repository.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapApi {

    static Retrofit retrofit;
    public final static  String URL="https://map.ir/search/";

    public static Retrofit getRetrofitMapInstance() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }
}
