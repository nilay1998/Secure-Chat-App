package com.example.demochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demochatapp.Service.Retrofit.NetworkClient;
import com.example.demochatapp.Service.Models.Profile;
import com.example.demochatapp.Service.Retrofit.RequestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    private EditText name_ET;
    private EditText email_ET;
    private EditText password_ET;
    private EditText phone_ET;
    private Button submit_BT;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        signup();
    }

    private void signup() {
        submit_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name=name_ET.getText().toString().trim();
                final String email=email_ET.getText().toString().trim();
                String password=password_ET.getText().toString().trim();
                String phone=phone_ET.getText().toString().trim();
                Retrofit retrofit = NetworkClient.getRetrofitClient();
                final RequestService requestService=retrofit.create(RequestService.class);
                Call<Profile> call=requestService.registerUser(name,email,password,phone);
                call.enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, Response<Profile> response) {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        if(response.body().getStatus().equals("1"))
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(SignUpActivity.this, ContactsActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Profile> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void initViews() {
        progressBar=findViewById(R.id.progressBar);
        name_ET=findViewById(R.id.signup_name);
        email_ET=findViewById(R.id.signup_email);
        password_ET=findViewById(R.id.signup_password);
        phone_ET=findViewById(R.id.signup_phone);
        submit_BT=findViewById(R.id.signup_button);
    }
}