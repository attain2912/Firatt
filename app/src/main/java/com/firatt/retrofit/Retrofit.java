package com.firatt.retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    public static Service service;

    public static Service getInstance(){
        if(service==null){
            retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service=retrofit.create(Service.class);
        }
        return  service;
    }
}
