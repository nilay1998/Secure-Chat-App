package com.example.demochatapp.Util;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketHelper {

    private Socket mSocket;
    private static final String TAG = "SocketConnection";
    private static SocketHelper instance;

    private SocketHelper(){
        {
            try {
                //mSocket = IO.socket("https://serene-sierra-89225.herokuapp.com");
                mSocket = IO.socket("http://192.168.0.104:3000");
            } catch (URISyntaxException e) {
                Log.e(TAG, "instance initializer: ERROR");
            }
        }
    }

    public static SocketHelper getInstance() {
        if(instance==null)
            instance=new SocketHelper();
        return instance;
    }

    public Socket getSocketConnection() {
        return mSocket;
    }
}
