package com.example.demochatapp.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demochatapp.Service.Models.Message;
import com.example.demochatapp.Service.Repositories.MessageRepo;

import java.util.ArrayList;

public class MessageActivityViewModel extends ViewModel {

    private static final String TAG = "MessageViewModel";
    private MutableLiveData<ArrayList<Message>> mMessages;
    private MessageRepo mRepo;
    private String senderEmail;
    private String receiverEmail;
    private MutableLiveData<String> receiverSocketID;
    private MutableLiveData<String> reveiverPublicKey;

    public void init(String sender,String receiver)
    {
        if(mMessages!=null)
            return;
        Log.e(TAG, "init:");
        mRepo=MessageRepo.getInstance();
        senderEmail=sender;
        receiverEmail=receiver;
        receiverSocketID=mRepo.getSocketID(receiverEmail);
        reveiverPublicKey=mRepo.getPublicKey(receiverEmail);
        mMessages=mRepo.getmMessages(senderEmail,receiverEmail);
    }

    public LiveData<String> getReceiverSocketID() {
        return receiverSocketID;
    }

    public LiveData<String> getReveiverPublicKey(){ return reveiverPublicKey; }

    public LiveData<ArrayList<Message>> getmMessages() {
        return mMessages;
    }

    public void addNewValue(Message message)
    {
        ArrayList<Message> msg=mMessages.getValue();
        msg.add(message);
        mMessages.setValue(msg);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "onCleared: ");
    }
}
