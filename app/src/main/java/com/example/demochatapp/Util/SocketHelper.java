package com.example.demochatapp.Util;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketHelper {

    private Socket mSocket;
    private static final String TAG = "SocketConnection";
    private static SocketHelper instance;

    private SocketHelper(String email){
        {
            try {
                //mSocket = IO.socket("https://serene-sierra-89225.herokuapp.com");

                IO.Options opts = new IO.Options();
                opts.query = "custom_id="+email;
                mSocket = IO.socket("http://192.168.0.104:3000",opts);
            } catch (URISyntaxException e) {
                Log.e(TAG, "instance initializer: ERROR");
            }
        }
    }

    public static SocketHelper getInstance(String email) {
        if(instance==null)
            instance=new SocketHelper(email);
        return instance;
    }

    public Socket getSocketConnection() {
        return mSocket;
    }
}
