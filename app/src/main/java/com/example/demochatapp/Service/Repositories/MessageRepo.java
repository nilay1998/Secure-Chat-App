package com.example.demochatapp.Service.Repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.demochatapp.Service.Models.Contacts;
import com.example.demochatapp.Service.Models.Message;
import com.example.demochatapp.Service.Models.Profile;
import com.example.demochatapp.Service.Retrofit.NetworkClient;
import com.example.demochatapp.Service.Retrofit.RequestService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageRepo {
    private static final String TAG = "MessageRepo";
    private static MessageRepo instance;
    private ArrayList<Message> mMessages;

    public static MessageRepo getInstance()
    {
        if(instance==null)
            instance=new MessageRepo();
        return instance;
    }

    public MutableLiveData<String> getSocketID(String email)
    {
        MutableLiveData<String> data=new MutableLiveData<>();
        data.setValue("");
        Retrofit retrofit=NetworkClient.getRetrofitClient();
        RequestService requestService=retrofit.create(RequestService.class);
        Call<Profile> call=requestService.getSocketID(email);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Log.e(TAG, "onResponse: "+response.body());
                data.setValue(response.body().getSocketID());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e(TAG, "onFailure: SSSS"+t.getMessage());
            }
        });

        return data;
    }

    public MutableLiveData<String> getPublicKey(String email){
        MutableLiveData<String> data=new MutableLiveData<>();
        data.setValue("");
        Retrofit retrofit=NetworkClient.getRetrofitClient();
        RequestService requestService=retrofit.create(RequestService.class);
        Call<Profile> call=requestService.getPublicKey(email);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Log.e(TAG, "onResponse: "+response.body());
                data.setValue(response.body().getPublickey());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e(TAG, "onFailure: SSSS"+t.getMessage());
            }
        });

        return data;
    }

    public MutableLiveData<ArrayList<Message>> getmMessages(String senderEmail,String receiverEmail) {
        MutableLiveData<ArrayList<Message>> data=new MutableLiveData<>();

        mMessages=new ArrayList<>();
        data.setValue(mMessages);

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        final RequestService requestService=retrofit.create(RequestService.class);
        Call<ArrayList<Message>> call=requestService.getMessages(senderEmail,receiverEmail);
        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                Log.e(TAG, "onResponse: ");
                mMessages.addAll(response.body());
                data.setValue(mMessages);
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });

        return data;
    }
}
