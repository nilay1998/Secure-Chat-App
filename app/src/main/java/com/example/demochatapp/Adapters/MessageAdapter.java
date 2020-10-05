package com.example.demochatapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demochatapp.R;
import com.example.demochatapp.Service.Models.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    private ArrayList<Message> messagesList = new ArrayList<>();
    static String email = "";

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessageAdapter(ArrayList<Message> m, String e) {
        messagesList = m;
        email = e;
    }

    @Override
    public int getItemViewType(int position) {
        //Log.e("MessageAdapter", email+" "+messagesList.get(position).getSenderEmail());
        if (email.equals(messagesList.get(position).getSenderEmail())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).message.setText(messagesList.get(position).getMessage());
                //((SentMessageHolder) holder).name.setText(messagesList.get(position).getNickname());
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).message.setText(messagesList.get(position).getMessage());
                //((ReceivedMessageHolder) holder).name.setText(messagesList.get(position).getNickname());
        }
    }
    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView message;

        SentMessageHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.direction);
            message = itemView.findViewById(R.id.message1);
        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView message;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.direction);
            message = itemView.findViewById(R.id.message1);
        }
    }
}