package com.example.demochatapp.Service.Retrofit;

import com.example.demochatapp.Service.Models.Contacts;
import com.example.demochatapp.Service.Models.Message;
import com.example.demochatapp.Service.Models.Profile;

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
                            @Field("password") String password);
    @POST("register")
    @FormUrlEncoded
    Call<Profile> registerUser(@Field("name") String name,
                               @Field("email") String email,
                               @Field("password") String password,
                               @Field("phone") String phone);
    @GET("getContacts")
    Call<ArrayList<Contacts>> getDatabaseContacts(@Query("phone") String[] phone);

    @GET("getMessages")
    Call<ArrayList<Message>> getMessages(@Query("sender") String sender, @Query("receiver") String receiver);
}
