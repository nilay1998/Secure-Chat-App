package com.example.demochatapp.Service.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {


    //public static String BASE_URL = "https://serene-sierra-89225.herokuapp.com/user/";
    public static String BASE_URL = "http://192.168.0.104:3000/user/";
    public static Retrofit retrofit;
    /*
    This public static method will return Retrofit client
    anywhere in the appplication
    */
    public static Retrofit getRetrofitClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
        //If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .client(okhttpClientBuilder.build())
                    .build();
        }
        return retrofit;
    }
}