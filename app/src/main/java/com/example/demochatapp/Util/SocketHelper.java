package com.example.demochatapp.Util;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketHelper {

    private Socket mSocket;
    private static final String TAG = "SocketConnection";
    private static SocketHelper instance;
    private static String email;

    private SocketHelper(){
        {
            try {
                //mSocket = IO.socket("https://serene-sierra-89225.herokuapp.com");

                IO.Options opts = new IO.Options();
                opts.query = "custom_id="+email;
                opts.forceNew=true;
                opts.reconnection=false;
                mSocket = IO.socket("http://192.168.0.104:3000",opts);
                Log.e(TAG, "SocketHelper: HANDSHAKE!! "+email);
            } catch (URISyntaxException e) {
                Log.e(TAG, "instance initializer: ERROR");
            }
        }
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        SocketHelper.email = email;
    }

    public static SocketHelper getInstance() {
        if(instance==null)
            instance=new SocketHelper();
        return instance;
    }

    public static SocketHelper newInstance(){
        return new SocketHelper();
    }

    public Socket getSocketConnection() {
        return mSocket;
    }
}
