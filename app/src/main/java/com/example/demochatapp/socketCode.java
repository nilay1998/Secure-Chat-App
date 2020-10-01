package com.example.demochatapp;

import android.util.Log;
import android.view.View;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class socketCode {

//    private Socket mSocket;
//    {
//        try {
//            mSocket = IO.socket("http://192.168.0.104:3000");
//        } catch (URISyntaxException e) {
//            Log.e(TAG, "instance initializer: ERROR");
//        }
//    }
//    mSocket.connect();
//    mSocket.emit("join",user_email);
//    button.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String receiver=reveiver_et.getText().toString();
//            mSocket.emit("messagedetection",email,receiver,"Hello");
//        }
//    });
//
//        mSocket.on("messageToUser", new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    try
//                    {
//                        String nickName=data.getString("sender");
//                        String msg=data.getString("message");
//                        textView.setText(nickName+" : "+msg);
//                        Log.e(TAG, "sender: "+nickName);
//                        Log.e(TAG, "Message: "+msg);
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    });

}
