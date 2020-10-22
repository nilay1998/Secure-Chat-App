package com.example.demochatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demochatapp.MessageActivity;
import com.example.demochatapp.R;
import com.example.demochatapp.Service.Models.Contacts;

import java.util.ArrayList;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{

    Context mContext;
    ArrayList<Contacts> mUserContacts;
    String mUserEmail;
    String mPrivateKey_RSA;
    String mPrivateKey_AES;
    public ContactsAdapter(Context mContext, ArrayList<Contacts> mUserContacts, String mUserEmail,String mPrivateKey,String aa)
    {
        this.mContext=mContext;
        this.mUserContacts=mUserContacts;
        this.mUserEmail=mUserEmail;
        this.mPrivateKey_RSA=mPrivateKey;
        mPrivateKey_AES=aa;
    }
    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_contacts_items,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, final int position) {
        holder.userName.setText(mUserContacts.get(position).getName());
        holder.contactsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("receiverName",mUserContacts.get(position).getName());
                intent.putExtra("senderEmail",mUserEmail);
                intent.putExtra("receiverEmail",mUserContacts.get(position).getEmail());
                intent.putExtra("privateKey_RSA",mPrivateKey_RSA);
                intent.putExtra("privateKey_AES",mPrivateKey_AES);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName;
        RelativeLayout contactsItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.contactName);
            contactsItem=itemView.findViewById(R.id.contactsItem);
        }
    }
}
