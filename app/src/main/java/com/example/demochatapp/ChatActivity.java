package com.example.demochatapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demochatapp.Retrofit.NetworkClient;
import com.example.demochatapp.Retrofit.Profile;
import com.example.demochatapp.Retrofit.RequestService;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends AppCompatActivity {
    private final String TAG="ChatAvtivity";
    private HashMap<String,String> contacts=new HashMap<>();
    private String user_email;
    private String[] phone;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final Intent intent=getIntent();
        user_email=intent.getStringExtra("email");
        getMyContacts();
        getDatabaseContacts();
    }

    private void getDatabaseContacts()
    {
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        final RequestService requestService=retrofit.create(RequestService.class);
        Call<Profile> call=requestService.getDatabaseContacts(phone);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response)
            {

            }
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
            }
        });
    }
    private void getMyContacts()
    {
        Cursor cursor_Android_Contacts = null;
        ContentResolver contentResolver = getContentResolver();
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
                            contacts.put(phoneNumber,contact_display_name);
                        }
                    }
                    phoneCursor.close();
                }
            }
        }
        int size=contacts.size();
        phone=new String[size];
        int index=0;
        for(Map.Entry<String,String> entry:contacts.entrySet()) {
            Log.e(TAG, entry.getKey() + " : " + entry.getValue());
            phone[index++]=entry.getKey();
        }
    }
}