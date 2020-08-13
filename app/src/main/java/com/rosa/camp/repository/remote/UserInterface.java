package com.rosa.camp.repository.remote;

import com.google.gson.JsonObject;
import com.rosa.camp.model.LoginUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserInterface  {

    @POST("RegisterUser")
    Call<JsonObject> RegisterUser (
            @Query("name") String name,
            @Query("family") String family,
            @Query("phone") String phone,
            @Query("email") String email,
            @Query("password") String password
    );

    @GET("LoginUser")
    Call<JsonObject> LoginUser(

           @Query("email") String email,
           @Query("password") String password
    );


    @GET("IsUSerExists")
    Call<JsonObject> IsUSerExists(@Query("email") String email);

    @GET("GetProfile")
    Call<JsonObject> GetProfile(
            @Query("email") String email
    );

    @PUT("UpdateProfile")
    Call<JsonObject> UpdateProfile(
            @Query("name") String name,
            @Query("family") String family,
            @Query("phone") String phone,
            @Query("sex") String sex,
            @Query("birthDate") String birthDate,
            @Query("email") String email

    );


}