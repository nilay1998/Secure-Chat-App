package com.example.demochatapp.ViewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demochatapp.Service.Models.Contacts;
import com.example.demochatapp.Service.Repositories.ContactsRepo;

import java.util.ArrayList;
import java.util.Map;

public class ContactsActivityViewModel extends ViewModel {
    private static final String TAG = "ContactsViewModel";
    private MutableLiveData<ArrayList<Contacts>> mContacts;
    private ContactsRepo mRepo;

    public void init(Context context) {
        if(mContacts!=null)
            return;
        Log.e(TAG, "init: ");
        mRepo=ContactsRepo.getInstance();
        mContacts=mRepo.getMachingContacts(context);
    }

    public LiveData<ArrayList<Contacts>> getmContacts() {
        return mContacts;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "onCleared: ");
    }
}
