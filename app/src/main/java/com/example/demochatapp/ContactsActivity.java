package com.example.demochatapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.demochatapp.Adapters.ContactsAdapter;
import com.example.demochatapp.Service.Models.Contacts;
import com.example.demochatapp.Service.Retrofit.NetworkClient;
import com.example.demochatapp.Service.Retrofit.RequestService;
import com.example.demochatapp.ViewModels.ContactsActivityViewModel;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactsActivity extends AppCompatActivity {
    private final String TAG="ChatAvtivity";
    private HashMap<String,String> contacts=new HashMap<>();
    private String user_email;
    private String[] phone;
    ArrayList<Contacts> contactsInDatabse=new ArrayList<>();
    RecyclerView recyclerView;
    ContactsAdapter adapter;
    private Sessions session;
    private ContactsActivityViewModel contactsActivityViewModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.e(TAG, "onCreate: ");
        recyclerView=findViewById(R.id.contactsList);

        final Intent intent=getIntent();
        user_email=intent.getStringExtra("email");

        contactsActivityViewModel =new ViewModelProvider(this).get(ContactsActivityViewModel.class);
        contactsActivityViewModel.init(getApplicationContext());
        contactsActivityViewModel.getmContacts().observe(this, new Observer<ArrayList<Contacts>>() {
            @Override
            public void onChanged(ArrayList<Contacts> contacts) {
                Log.e(TAG, "onChanged: ViewModel "+contacts.size());
                adapter.notifyDataSetChanged();
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter=new ContactsAdapter(this,contactsActivityViewModel.getmContacts().getValue(),user_email);
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
        session=new Sessions(getApplicationContext());
        session.killSession();
        Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
    }
}