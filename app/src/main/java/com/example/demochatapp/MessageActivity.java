package com.example.demochatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demochatapp.Adapters.MessageAdapter;
import com.example.demochatapp.Service.Models.Message;
import com.example.demochatapp.Service.Retrofit.NetworkClient;
import com.example.demochatapp.Service.Retrofit.RequestService;
import com.example.demochatapp.ViewModels.MessageActivityViewModel;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageActivity extends AppCompatActivity {
    private String receiverEmail;
    private String receiverName;
    private String senderEmail;
    private Socket mSocket;

    private Button send_button;
    private EditText msg_editText;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private ArrayList<Message> messageArrayList=new ArrayList<>();
    private MessageActivityViewModel messageActivityViewModel;
    private static final String TAG = "MessageActivity";

    {
        try {
            mSocket = IO.socket("http://192.168.0.104:3000");
        } catch (URISyntaxException e) {
            Log.e(TAG, "instance initializer: ERROR");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent=getIntent();
        receiverEmail=intent.getStringExtra("receiverEmail");
        receiverName=intent.getStringExtra("receiverName");
        senderEmail=intent.getStringExtra("senderEmail");


        run_socket();

        messageActivityViewModel=new ViewModelProvider(this).get(MessageActivityViewModel.class);
        messageActivityViewModel.init(senderEmail,receiverEmail);
        messageActivityViewModel.getmMessages().observe(this, new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                Log.e(TAG, "onChanged: ");
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
                adapter.notifyDataSetChanged();
            }
        });

        initviews();
    }


    private void initviews()
    {
        msg_editText=findViewById(R.id.message2);
        send_button=findViewById(R.id.send);

        recyclerView=findViewById(R.id.messageList);
        adapter=new MessageAdapter(messageActivityViewModel.getmMessages().getValue(),senderEmail);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void run_socket()
    {
        mSocket.connect();
        mSocket.emit("join",senderEmail);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=msg_editText.getText().toString().trim();
                if(!msg.isEmpty() || msg.equals("")) {
                    mSocket.emit("messagedetection", senderEmail, receiverEmail, msg);
                    Message m=new Message(senderEmail,msg,receiverEmail);
                    messageArrayList.add(m);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount());
                    adapter.notifyDataSetChanged();
                    Log.e(TAG, "onClick: "+msg+" : sent to :"+receiverEmail);
                }
                msg_editText.setText("");
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
                            String sender=data.getString("sender");
                            Log.e(TAG, "sender: "+sender);
                            String msg=data.getString("message");
                            Log.e(TAG, "Message: "+msg);
                            String receiver=data.getString("receiver");
                            Log.e(TAG, "Receiver: "+receiver);
                            Message m=new Message(sender,msg,receiver);
                            messageArrayList.add(m);
                            recyclerView.smoothScrollToPosition(adapter.getItemCount());
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        mSocket.disconnect();
    }
}