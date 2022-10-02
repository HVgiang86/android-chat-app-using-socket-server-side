package com.example.serverchatapp.socket_server;

import android.os.Environment;
import android.util.Log;

import com.example.serverchatapp.model.Message;
import com.example.serverchatapp.model.MessageManager;
import com.example.serverchatapp.my_socket_library.IO;
import com.example.serverchatapp.my_socket_library.SingleSocket;
import com.example.serverchatapp.my_socket_library.model.MessagePackage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class OnNewMessageListener implements IO.OnNewMessageListener {
    private static final String TAG = "New Message";

    @Override
    public void onNewMessage(SingleSocket socket, MessagePackage messagePackage) {
        if (messagePackage.isFile()) {
            saveFileFromStream(messagePackage);
        } else {
            if (messagePackage.getEvent().equalsIgnoreCase(IO.SEND_MESSAGE)) {
                Message message = new Message(messagePackage.getMessage(), false, false);
                MessageManager.getInstance().addMessage(message);
            } else {
                socket.disconnectListener.onDisconnect(socket);
            }
        }
    }

    private void saveFileFromStream(MessagePackage message) {
        String filename = message.getMessage();

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        Log.d(TAG, "File name: " + message.getMessage());
        Log.d(TAG, "File path: " + dir.getPath());

        if(dir.exists()) {
            dir.mkdirs();
        }

        try {
            File file = new File(dir.getPath(),filename);


            byte[] bytes = message.getData();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            bos.close();
            Message msg = new Message(file.getPath(), false, true);
            MessageManager.getInstance().addMessage(msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
