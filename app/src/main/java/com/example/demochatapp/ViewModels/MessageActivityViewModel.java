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
    private MutableLiveData<String> reveiverRSAPublicKey;
    private MutableLiveData<String> reveiverAESPublicKey;
    private MutableLiveData<String> lastSeen;

    public void init(String sender,String receiver)
    {
        if(mMessages!=null)
            return;
        Log.e(TAG, "init:");
        mRepo=MessageRepo.getInstance();
        senderEmail=sender;
        receiverEmail=receiver;
        receiverSocketID=mRepo.getSocketID(receiverEmail);
        reveiverRSAPublicKey=mRepo.getPublicKeyRSA(receiverEmail);
        reveiverAESPublicKey=mRepo.getPublicKeyAES(receiverEmail);
        lastSeen=mRepo.getlastSeen(receiverEmail);
        mMessages=mRepo.getmMessages(senderEmail,receiverEmail);
    }

    public MutableLiveData<String> getLastSeen() {
        return lastSeen;
    }

    public LiveData<String> getReceiverSocketID() {
        return receiverSocketID;
    }

    public LiveData<String> getReveiverRSAPublicKey(){ return reveiverRSAPublicKey; }

    public MutableLiveData<String> getReveiverAESPublicKey() {
        return reveiverAESPublicKey;
    }

    public LiveData<ArrayList<Message>> getmMessages() {
        return mMessages;
    }

    public void addNewValue(Message message)
    {
        ArrayList<Message> msg=mMessages.getValue();
        msg.add(message);
        mMessages.setValue(msg);
    }

    public void setPrevMessagesAsRead(){
        ArrayList<Message> msg=mMessages.getValue();
        int size=msg.size();
        for(int i=size-1;i>=0;i--)
        {
            if(msg.get(i).getIsRead().equals("1"))
                break;
            msg.get(i).setIsRead("1");
        }
        mMessages.postValue(msg);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "onCleared: ");
    }
}
