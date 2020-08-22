package com.rosa.camp.repository.remote;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MapInterface {

    @GET("v2")
    Call<JsonObject> v2(

            @Query("text") String text

    );

}
