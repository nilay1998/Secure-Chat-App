package com.example.demochatapp.Retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestService
{
    @POST("login")
    @FormUrlEncoded
    Call<Profile> loginUser(@Field("email") String email,
                             @Field("password") String password );
    @GET("getContacts")
    Call<ArrayList<Contacts>> getDatabaseContacts(@Query("phone") String[] phone);
}
