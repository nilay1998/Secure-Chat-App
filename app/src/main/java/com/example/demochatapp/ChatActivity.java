package com.example.demochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;

public class ChatActivity extends AppCompatActivity {

    private Socket mSocket;
    private final String TAG="MainActivity";

    {
        try {
            mSocket = IO.socket("http://192.168.0.104:3000");
        } catch (URISyntaxException e) {
            Log.e(TAG, "instance initializer: ERROR");
        }
    }
    private TextView textView;
    private Button button;
    private EditText reveiver_et;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        textView=findViewById(R.id.text_tv);
        button=findViewById(R.id.send_button);
        reveiver_et=findViewById(R.id.receiver_et);
        final Intent intent=getIntent();
        final String email=intent.getStringExtra("email");
        textView.setText(email);

        mSocket.connect();
        mSocket.emit("join",email);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiver=reveiver_et.getText().toString();
                mSocket.emit("messagedetection",email,receiver,"Hello");
            }
        });

        mSocket.on("messageToUser", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try
                        {
                            String nickName=data.getString("sender");
                            String msg=data.getString("message");
                            textView.setText(nickName+" : "+msg);
                            Log.e(TAG, "sender: "+nickName);
                            Log.e(TAG, "Message: "+msg);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}