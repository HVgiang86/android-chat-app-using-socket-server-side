package com.example.serverchatapp.model;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.serverchatapp.activity.MainActivity;
import com.example.serverchatapp.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private final static MessageManager INSTANCE = new MessageManager();
    private final List<Message> messageList;
    private MainActivity activity;
    private RecyclerView rv;
    private MessageAdapter adapter;

    private MessageManager() {
        messageList = new ArrayList<>();
        messageList.add(new Message("Welcome back!", false, false));
    }

    public static MessageManager getInstance() {
        return INSTANCE;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setRv(RecyclerView rv) {
        this.rv = rv;
    }

    public void addMessage(Message message) {
        messageList.add(message);
        if (adapter != null) {
            activity.runOnUiThread(() -> {
                Log.d("Server log", "Message List update");
                adapter.notifyItemInserted(messageList.size() - 1);
                rv.scrollToPosition(messageList.size() - 1);
            });
        }
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setAdapter(MessageAdapter adapter) {
        this.adapter = adapter;
    }

}
