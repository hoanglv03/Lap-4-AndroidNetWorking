package com.example.lap4_android_networking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestInterface {

    @POST("register")
    Call<User> register(@Body User user);

    @POST("login")
    Call<User> login(@Body User user);

    @PUT("changePassword/{id}")
    Call<User> changePassword(@Path("id") String id, @Body User user);
}
