package com.firatt.retrofit;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Service {
    @Headers({"Content-Type:application/json",
            "Authorization:key=AIzaSyDLyciSdIrJ-BXfnhobmQ0Hf14xDgPwjps"})
    @POST("fcm/send")
    Call<Map> addnotifi(@Body HashMap<String, String> datum);
}
