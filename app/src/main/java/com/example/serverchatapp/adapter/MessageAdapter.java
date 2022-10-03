package com.example.serverchatapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serverchatapp.R;
import com.example.serverchatapp.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final static int SERVER_MESSAGE_TYPE = 1;
    private final static int SERVER_FILE_TYPE = 3;
    private final static int CLIENT_FILE_TYPE = 4;
    private final static int CLIENT_MESSAGE_TYPE = 2;
    private final Context context;
    private final List<Message> messageList;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if (viewType == SERVER_MESSAGE_TYPE)
            v = inflater.inflate(R.layout.server_message_item, parent, false);
        else if (viewType == CLIENT_MESSAGE_TYPE)
            v = inflater.inflate(R.layout.client_message_item, parent, false);
        else if (viewType == SERVER_FILE_TYPE)
            v = inflater.inflate(R.layout.server_file_item, parent, false);
        else v = inflater.inflate(R.layout.client_file_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        Log.d("Adapter log", "Item: Sender: " + message.getSender() + " Message: " + message.getContent());
        if (getItemViewType(position) != SERVER_FILE_TYPE || getItemViewType(position) != SERVER_FILE_TYPE) {
            String sender = message.getSender();
            if (sender != null)
                holder.senderTv.setText(sender.trim());

        }

        String content = message.getContent().trim();
        if (getItemViewType(position) == CLIENT_MESSAGE_TYPE || getItemViewType(position) == SERVER_MESSAGE_TYPE)
            holder.contentTv.setText(content);

        if (getItemViewType(position) == SERVER_FILE_TYPE || getItemViewType(position) == CLIENT_FILE_TYPE) {
            int i = content.lastIndexOf("/");
            String filename = content.substring(i + 1);
            holder.contentTv.setText(filename);
            holder.contentTv.setOnClickListener((v) -> {
                showFileChooser(content);
                Log.d("Adapter log", "FILE CLICKED!");
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).isServer()) {
            if (messageList.get(position).isFile()) return SERVER_FILE_TYPE;
            return SERVER_MESSAGE_TYPE;
        } else {
            if (messageList.get(position).isFile()) return CLIENT_FILE_TYPE;
            return CLIENT_MESSAGE_TYPE;
        }
    }

    private void showFileChooser(String filePath) {

        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(filePath));
        baseIntent.setType(type);

        int i = filePath.lastIndexOf("/");
        Uri selectedUri = Uri.parse(filePath.substring(0, i));
        Log.d("Adapter Log", "test path: " + selectedUri);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "*/*");

        if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null) {
            context.startActivity(intent);
        } else {
            if (baseIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(baseIntent);
            } else {
                // if you reach this place, it means there is no any file
                // explorer app installed on your device
                Toast.makeText(context.getApplicationContext(), "File unable to open", Toast.LENGTH_LONG).show();
            }

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contentTv;
        public TextView senderTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTv = itemView.findViewById(R.id.message_content);
            senderTv = itemView.findViewById(R.id.sender_name);
        }
    }
}