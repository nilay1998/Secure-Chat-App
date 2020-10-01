package com.example.demochatapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demochatapp.Retrofit.NetworkClient;
import com.example.demochatapp.Retrofit.Profile;
import com.example.demochatapp.Retrofit.RequestService;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private EditText email_editText;
    private EditText password_ediText;
    private Button submit_button;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();
        initViews();
        login();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermission()
    {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    Toast.makeText(this,"Request Granted",Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Request Denied",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    private void login()
    {
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=email_editText.getText().toString();
                final String password=password_ediText.getText().toString();
                Retrofit retrofit = NetworkClient.getRetrofitClient();
                final RequestService requestService=retrofit.create(RequestService.class);
                Call<Profile> call=requestService.loginUser(email,password);
                call.enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, Response<Profile> response)
                    {
                        Toast.makeText(getApplicationContext(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        if(response.body().getStatus().equals("1"))
                        {
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(Call<Profile> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initViews()
    {
        email_editText=findViewById(R.id.email_et);
        password_ediText=findViewById(R.id.password_et);
        submit_button=findViewById(R.id.submit_button);
    }
}