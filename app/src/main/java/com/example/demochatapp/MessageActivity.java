package com.example.demochatapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.demochatapp.Adapters.MessageAdapter;
import com.example.demochatapp.Service.Models.Message;
import com.example.demochatapp.Util.AESUtil;
import com.example.demochatapp.Util.RSAUtil;
import com.example.demochatapp.Util.SocketHelper;
import com.example.demochatapp.ViewModels.MessageActivityViewModel;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;



public class MessageActivity extends AppCompatActivity {
    private String receiverEmail;
    private String receiverName;
    private String senderEmail;
    private Socket mSocket;

    private Button send_button;
    private EditText msg_editText;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private Toolbar toolbar;
    private TextView toolbar_name;
    private TextView toolbaar_lastseen;

    private ArrayList<Message> messageArrayList=new ArrayList<>();
    private MessageActivityViewModel messageActivityViewModel;
    private static final String TAG = "MessageActivity";
    private String receiverSocketID;
    private String reveiverPublicKey_RSA;
    private String senderPrivateKey_RSA;
    private String lastSeen="";

    private String reveiverPublicKey_AES;
    private String senderPrivateKey_AES;
    private byte[] sharedSecretKey_AES;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent=getIntent();
        receiverEmail=intent.getStringExtra("receiverEmail");
        receiverName=intent.getStringExtra("receiverName");
        senderEmail=intent.getStringExtra("senderEmail");
        senderPrivateKey_RSA=intent.getStringExtra("privateKey_RSA");
        senderPrivateKey_AES=intent.getStringExtra("privateKey_AES");

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
        messageActivityViewModel.getReceiverSocketID().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.length()>0)
                    progressBar.setVisibility(View.INVISIBLE);
                receiverSocketID=s;
            }
        });
        messageActivityViewModel.getReveiverRSAPublicKey().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                reveiverPublicKey_RSA=s;
            }
        });

        messageActivityViewModel.getReveiverAESPublicKey().observe(this, new Observer<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(String s) {
                reveiverPublicKey_AES=s;
                if(s.length()>0)
                {
                    try {
                        sharedSecretKey_AES= AESUtil.getSharedSecretKey(senderPrivateKey_AES,reveiverPublicKey_AES);
                        adapter=new MessageAdapter(messageActivityViewModel.getmMessages().getValue(),senderEmail,senderPrivateKey_RSA,sharedSecretKey_AES);
                        recyclerView.setAdapter(adapter);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        messageActivityViewModel.getLastSeen().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.length()>0 && !s.equals("Online"))
                {
                    Timestamp timestamp=new Timestamp(Long.parseLong(s));
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
                    Log.e(TAG, "call: "+sdf.format(timestamp));
                    toolbaar_lastseen.setText("last seen "+sdf.format(timestamp));
                }
                else
                    toolbaar_lastseen.setText(s);
            }
        });


        initviews();
        run_socket();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initviews()
    {
        msg_editText=findViewById(R.id.message2);
        send_button=findViewById(R.id.send);
        toolbar=findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar_name=findViewById(R.id.user_name); toolbar_name.setText(receiverName);
        toolbaar_lastseen=findViewById(R.id.user_status);
        recyclerView=findViewById(R.id.messageList);
        adapter=new MessageAdapter(messageActivityViewModel.getmMessages().getValue(),senderEmail,senderPrivateKey_RSA,sharedSecretKey_AES);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void run_socket()
    {
        //SocketHelper socketHelper=SocketHelper.newInstance();
        SocketHelper.setEmail(senderEmail);
        SocketHelper socketHelper=SocketHelper.newInstance();
        mSocket= socketHelper.getSocketConnection();
        mSocket.connect();
        send_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String msg=msg_editText.getText().toString().trim();
                if(!msg.isEmpty() || msg.equals("")) {

                    try {
//                        String encrypted_msg= Base64.getEncoder().encodeToString(RSAUtil.encrypt(msg,reveiverPublicKey_RSA));
                        String encrypted_msg= Base64.getEncoder().encodeToString(AESUtil.encrypt(msg,sharedSecretKey_AES));

                        mSocket.emit("messagedetection", senderEmail, receiverEmail, encrypted_msg,receiverSocketID);
                        Message m=new Message(senderEmail,encrypted_msg,receiverEmail);
                        messageActivityViewModel.addNewValue(m);
                        Log.e(TAG, "onClick: "+msg+" : sent to :"+receiverEmail);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
//                    catch (BadPaddingException e) {
//                        e.printStackTrace();
//                    } catch (IllegalBlockSizeException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchPaddingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    }
                }
                msg_editText.setText("");
            }
        });

        mSocket.on("messageToUser", messageToUserEvent);


        mSocket.on("activeStatus",lastSeenEvent);

//        mSocket.on(receiverEmail+"socketUpdate",socketIdUpdationEvent);
    }

    private Emitter.Listener lastSeenEvent=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.e(TAG, "ONLINE OFFLINE ACTICITY HAPPENING ");
            try {
                messageActivityViewModel.getLastSeen().postValue(data.getString("status"));
            }

            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener socketIdUpdationEvent=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                receiverSocketID=data.getString("id");
            }

            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private Emitter.Listener messageToUserEvent= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
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

//                            msg=RSAUtil.decrypt(msg,senderPrivateKey_RSA);
                        //msg=AESUtil.decrypt(msg,sharedSecretKey_AES);
                        Message m=new Message(sender,msg,receiver);
                        messageArrayList.add(m);
                        messageActivityViewModel.addNewValue(m);
                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                    }

//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        } catch (InvalidKeyException e) {
//                            e.printStackTrace();
//                        } catch (NoSuchPaddingException e) {
//                            e.printStackTrace();
//                        } catch (BadPaddingException e) {
//                            e.printStackTrace();
//                        } catch (IllegalBlockSizeException e) {
//                            e.printStackTrace();
//                        }
                }
            });
        }
    };

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
        mSocket.off("messageToUser", messageToUserEvent);
        mSocket.off(receiverEmail+"socketUpdate",socketIdUpdationEvent);
        Log.e(TAG, "onDestroy: ");
    }
}