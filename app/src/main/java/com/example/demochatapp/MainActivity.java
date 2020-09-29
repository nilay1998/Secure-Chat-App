package com.example.demochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private EditText email_editText;
    private EditText password_ediText;
    private Button submit_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_editText.getText().toString();
                String password=password_ediText.getText().toString();
                Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("email",email);
                //intent.putExtra("password",0);
                startActivity(intent);
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