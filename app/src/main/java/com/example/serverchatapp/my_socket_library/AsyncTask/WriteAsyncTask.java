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
        MessagePackage messagePackage = messagePackages[0];
        Log.d(TAG, "Data to send: " + messagePackage.toString());

        try {
            OutputStream os = mSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeBytes(messagePackage.getEvent() + "\n");

            if (messagePackage.isFile()) {
                dos.writeBytes(messagePackage.getDataSizeInByte() + "\n");
                dos.writeBytes(messagePackage.getMessage() + "\n");
                dos.write(messagePackage.getData(), 0, messagePackage.getDataSizeInByte());
            } else {
                dos.writeUTF(messagePackage.getMessage()+"\n");
                //dos.writeBytes(messagePackage.getMessage() + "\n");
            }

            Log.d(TAG, "transferred");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Data transferred fail!");
        }

        /*String sentence_to_server = "";
        String controlByte = strings[0];
        if (strings.length >1)  sentence_to_server = strings[1];

        try {
            Log.d("Server Log", "Data to send: " + sentence_to_server);
            OutputStream os = socket.getOutputStream();
            DataOutputStream outToServer =
                    new DataOutputStream(os);

            //Tạo inputStream nối với Socket
            BufferedReader inFromServer =
                    new BufferedReader(new
                            InputStreamReader(socket.getInputStream()));
//
            //Gửi chuỗi ký tự tới Server thông qua outputStream đã nối với Socket (ở trên)
            os.write(Integer.parseInt(controlByte));
            outToServer.writeBytes(sentence_to_server + '\n');*/

        return null;
    }

}
