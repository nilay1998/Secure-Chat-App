package com.example.demochatapp.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestService
{
    @POST("login")
    @FormUrlEncoded
    Call<Profile> loginUser(@Field("email") String email,
                             @Field("password") String password );
}
