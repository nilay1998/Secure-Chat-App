package com.example.demochatapp.Service.Repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demochatapp.Adapters.ContactsAdapter;
import com.example.demochatapp.Service.Models.Contacts;
import com.example.demochatapp.Service.Models.Profile;
import com.example.demochatapp.Service.Retrofit.NetworkClient;
import com.example.demochatapp.Service.Retrofit.RequestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactsRepo {
    private static final String TAG = "ContactsRepo";
    private static ContactsRepo instance;
    private ArrayList<Contacts> contactsInDatabse;

    public static ContactsRepo getInstance()
    {
        if(instance==null)
            instance=new ContactsRepo();
        return instance;
    }

    public void addRSAPublicKeyToDatabase(String publicKey,String user_email) {
        Retrofit retrofit=NetworkClient.getRetrofitClient();
        RequestService requestService=retrofit.create(RequestService.class);
        Call<Profile> call=requestService.setPublicKeyRSA(publicKey,user_email);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Log.e(TAG, "onResponse: "+response.body().getMessage());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void addAESPublicKeyToDatabase(String publicKey,String user_email) {
        Retrofit retrofit=NetworkClient.getRetrofitClient();
        RequestService requestService=retrofit.create(RequestService.class);
        Call<Profile> call=requestService.setPublicKeyAES(publicKey,user_email);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Log.e(TAG, "onResponse: "+response.body().getMessage());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public MutableLiveData<ArrayList<Contacts>> getMachingContacts(Context context)
    {
        MutableLiveData<ArrayList<Contacts>> data=new MutableLiveData<>();
        contactsInDatabse=new ArrayList<>();
        data.setValue(contactsInDatabse);
        HashMap<String,String> phoneContacts=getPhoneContacts(context);

        String[] phoneNumbers=new String[phoneContacts.size()];
        int index=0;
        for(Map.Entry<String,String> entry:phoneContacts.entrySet())
        {
            Log.e(TAG, "getMachingContacts: "+entry.getKey()+" "+entry.getValue());
            phoneNumbers[index++]=entry.getKey();
        }

        Retrofit retrofit=NetworkClient.getRetrofitClient();
        RequestService requestService=retrofit.create(RequestService.class);
        Call<ArrayList<Contacts>> call=requestService.getDatabaseContacts(phoneNumbers);
        call.enqueue(new Callback<ArrayList<Contacts>>() {
            @Override
            public void onResponse(Call<ArrayList<Contacts>> call, Response<ArrayList<Contacts>> response) {
                Log.e(TAG, "onResponse: "+ response.code());
                if(response.isSuccessful() && response.code()==200 && response.body()!=null)
                {
                    contactsInDatabse.addAll(response.body());
                    for(int i=0;i<contactsInDatabse.size();i++)
                    {
                        Contacts curr=contactsInDatabse.get(i);
                        curr.setName(phoneContacts.get(curr.getPhone()));
                        Log.e(TAG, "onResponse: "+curr.getName()+" "+curr.getPhone());
                    }
                    data.setValue(contactsInDatabse);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Contacts>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
            }
        });

        return data;
    }


    public HashMap<String,String>  getPhoneContacts(Context context)
    {
        HashMap<String,String> contacts=new HashMap<>();
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            cursor_Android_Contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }

        if (cursor_Android_Contacts.getCount() > 0)
        {
            while (cursor_Android_Contacts.moveToNext()) {
                String contact_id = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_display_name = cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor_Android_Contacts.getString(cursor_Android_Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0)
                {
                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);

                    while (phoneCursor.moveToNext())
                    {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneNumber=phoneNumber.replaceAll("\\s", "");
                        if(phoneNumber.length()>=10) {
                            phoneNumber=phoneNumber.substring(phoneNumber.length()-10,phoneNumber.length());
                            boolean flag=true;
                            for(int i=0;i<10;i++)
                            {
                                if(!Character.isDigit(phoneNumber.charAt(i)))
                                {
                                    flag=false;
                                    break;
                                }
                            }
                            if(flag)
                                contacts.put(phoneNumber,contact_display_name);
                        }
                    }
                    phoneCursor.close();
                }
            }
        }
        return contacts;
    }
}
