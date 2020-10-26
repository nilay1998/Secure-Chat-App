package com.example.demochatapp.ViewModels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demochatapp.Service.Models.Profile;
import com.example.demochatapp.Service.Repositories.ContactsRepo;

import java.util.ArrayList;

public class ContactsActivityViewModel extends ViewModel {
    private static final String TAG = "ContactsViewModel";
    private MutableLiveData<ArrayList<Profile>> mContacts;
    private ContactsRepo mRepo;

    public void init(Context context) {
        if(mContacts!=null)
            return;
        Log.e(TAG, "init: ");
        mRepo=ContactsRepo.getInstance();
        mContacts=mRepo.getMachingContacts(context);
    }

    public LiveData<ArrayList<Profile>> getmContacts() {
        return mContacts;
    }

     public void addRSAPublicKeyToDatabase(String publicKey,String email) {
        mRepo.addRSAPublicKeyToDatabase(publicKey,email);
    }

    public void addAESPublicKeyToDatabase(String publicKey,String email) {
        mRepo.addAESPublicKeyToDatabase(publicKey,email);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "onCleared: ");
    }
}
