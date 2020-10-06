package com.rosa.camp.repository.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

   static  Retrofit retrofit;
   public final static  String URL="http://192.168.30.5:8080/Api/UserAuthentication/";

    public static Retrofit getRetrofitInstance(){

        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }




}
