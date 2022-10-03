package com.example.serverchatapp.my_socket_library.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.serverchatapp.my_socket_library.model.MessagePackage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class WriteAsyncTask extends AsyncTask<MessagePackage, Void, Void> {
    private final static String TAG = "Transferring Log";
    private final Socket mSocket;

    public WriteAsyncTask(Socket mSocket) {
        this.mSocket = mSocket;
    }

    @Override
    protected Void doInBackground(MessagePackage... messagePackages) {
        //Here we send an message package to server
        //Message package's content will be sent in the following order: event -> sender -> fileSize (if file)
        // -> message -> data in byte array (if file), each properties will be sent on a new line

        //message package to send
        MessagePackage messagePackage = messagePackages[0];

        try {
            OutputStream os = mSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.flush();

            //write event
            dos.writeBytes(messagePackage.getEvent() + "\n");

            //write sender name
            dos.writeUTF(messagePackage.getSender() + '\n');

            if (messagePackage.isFile()) {
                //write data size in byte
                dos.writeBytes(messagePackage.getDataSizeInByte() + "\n");

                //write file name
                dos.writeBytes(messagePackage.getMessage() + "\n");

                //write data in byte array
                byte[] data = messagePackage.getData();
                os.write(data, 0, messagePackage.getDataSizeInByte());
            } else {
                //write message
                dos.writeUTF(messagePackage.getMessage() + "\n");
//                dos.writeBytes(messagePackage.getMessage() + "\n");
            }

            Log.d(TAG, "transferred");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Data transferred fail!");
        }
        return null;
    }

}
