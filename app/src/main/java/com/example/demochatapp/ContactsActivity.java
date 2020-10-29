package com.example.demochatapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.demochatapp.Adapters.ContactsAdapter;
import com.example.demochatapp.Service.Models.Profile;
import com.example.demochatapp.Util.DH_KeyPairGenerator;
import com.example.demochatapp.Util.RSAKeyPairGenerator;
import com.example.demochatapp.Util.SocketHelper;
import com.example.demochatapp.ViewModels.ContactsActivityViewModel;
import com.github.nkzawa.socketio.client.Socket;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class ContactsActivity extends AppCompatActivity {
    private final String TAG="ChatAvtivity";
    private String user_email;
    RecyclerView recyclerView;
    ContactsAdapter adapter;
    private Sessions session;
    private ContactsActivityViewModel contactsActivityViewModel;
    private Socket mSocket;
    private String publicKeyRSA;
    private String privateKeyRSA;

    private String publicKeyAES;
    private String privateKeyAES;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.e(TAG, "onCreate: ");
        recyclerView=findViewById(R.id.contactsList);
        progressBar=findViewById(R.id.progressBar);

        final Intent intent=getIntent();
        user_email=intent.getStringExtra("email");

        contactsActivityViewModel =new ViewModelProvider(this).get(ContactsActivityViewModel.class);
        contactsActivityViewModel.init(getApplicationContext());
        contactsActivityViewModel.getmContacts().observe(this, new Observer<ArrayList<Profile>>() {
            @Override
            public void onChanged(ArrayList<Profile> contacts) {
                Log.e(TAG, "onChanged: ViewModel "+contacts.size());
                if(contacts.size()==0)
                    progressBar.setVisibility(View.VISIBLE);
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    String[] email=new String[contacts.size()];
                    for(int i=0;i<contacts.size();i++)
                        email[i]=contacts.get(i).getEmail();
                    mSocket.emit("joinRoom",user_email,email);
                }
                adapter.notifyDataSetChanged();
            }
        });

        try {
            generateKeys();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        initRecyclerView();
        initSocketConnection();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void generateKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        session=new Sessions(getApplicationContext());
        if(!session.getValue("publicKeyRSA").equals("-1")) {
            publicKeyRSA = session.getValue("publicKeyRSA");
            privateKeyRSA = session.getValue("privateKeyRSA");

            publicKeyAES=session.getValue("publicKeyAES");
            privateKeyAES=session.getValue("privateKeyAES");
            Log.e(TAG, "Public AES: "+publicKeyAES);
            Log.e(TAG, "Private AES: "+privateKeyAES);
        }
        else
        {
            RSAKeyPairGenerator rsaKeyPairGenerator=new RSAKeyPairGenerator();
            publicKeyRSA=rsaKeyPairGenerator.getPublicKey_RSA();
            privateKeyRSA=rsaKeyPairGenerator.getPrivateKey_RSA();
            session.setValue("publicKeyRSA",publicKeyRSA);
            session.setValue("privateKeyRSA",privateKeyRSA);
            contactsActivityViewModel.addRSAPublicKeyToDatabase(publicKeyRSA,user_email);

            DH_KeyPairGenerator dhKeyPairGenerator=new DH_KeyPairGenerator();
            privateKeyAES=dhKeyPairGenerator.getPrivateKey_AES();
            publicKeyAES=dhKeyPairGenerator.getPublicKey_AES();
//            privateKeyAES=publicKeyAES="abcd";
            session.setValue("publicKeyAES",publicKeyAES);
            session.setValue("privateKeyAES",privateKeyAES);
            Log.e(TAG, "DH Public Key: "+publicKeyAES);
            Log.e(TAG, "DH Private Key: "+privateKeyAES);
            contactsActivityViewModel.addAESPublicKeyToDatabase(publicKeyAES,user_email);
        }
    }



    private void initSocketConnection() {
        SocketHelper.setEmail(user_email);
        SocketHelper socketHelper=SocketHelper.newInstance();
        mSocket= socketHelper.getSocketConnection();
        mSocket.connect();
        mSocket.emit("join",user_email);
    }
    private void initRecyclerView() {
        adapter=new ContactsAdapter(this,contactsActivityViewModel.getmContacts().getValue(),user_email,privateKeyRSA,privateKeyAES);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {

        session.killSession();
        Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
        startActivity(intent);
        killActivity();
    }

    private void killActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        killActivity();
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
        mSocket.disconnect();
        mSocket.close();
        Log.e(TAG, "onDestroy: LLLLLLLLLLLLLL");
    }
}