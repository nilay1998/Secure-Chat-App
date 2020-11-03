package com.example.demochatapp.Service.Retrofit;

import com.example.demochatapp.Service.Models.Message;
import com.example.demochatapp.Service.Models.Profile;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @POST("getContacts")
    @FormUrlEncoded
    Call<ArrayList<Profile>> getDatabaseContacts(@Field("phone") String[] phone);

    @GET("getMessages")
    Call<ArrayList<Message>> getMessages(@Query("sender") String sender, @Query("receiver") String receiver);

    @GET("getSocketID")
    Call<Profile> getSocketID(@Query("email") String email);

    @GET("getlastSeen")
    Call<Profile> getlastSeen(@Query("email") String email);

    @GET("getPublicKeyRSA")
    Call<Profile> getPublicKeyRSA(@Query("email") String email);

    @PUT("setPublicKeyRSA")
    @FormUrlEncoded
    Call<Profile> setPublicKeyRSA(@Field("publickeyRSA") String publicKey,@Field("email") String email);

    @GET("getPublicKeyAES")
    Call<Profile> getPublicKeyAES(@Query("email") String email);

    @PUT("setPublicKeyAES")
    @FormUrlEncoded
    Call<Profile> setPublicKeyAES(@Field("publickeyAES") String publicKey,@Field("email") String email);
}
