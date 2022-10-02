package com.example.serverchatapp.my_socket_library.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.serverchatapp.my_socket_library.IO;
import com.example.serverchatapp.my_socket_library.SingleSocket;
import com.example.serverchatapp.my_socket_library.model.MessagePackage;
import com.example.serverchatapp.my_socket_library.model.MessagePackageBuilder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final static String TAG = "Data Received Tag";
    private final SingleSocket socket;

    public ReadAsyncTask(SingleSocket socket) {
        this.socket = socket;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            while (true) {
                InputStream is = socket.getSocket().getInputStream();
                DataInputStream dis = new DataInputStream(is);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                Log.d(TAG, "New Data");
                MessagePackageBuilder builder = new MessagePackageBuilder();
                String event = dis.readLine();
                builder.setEvent(event);
                Log.d(TAG, "Event: " + event);
                if (event.equalsIgnoreCase(IO.SEND_FILE)) {
                    builder.setType(IO.SEND_FILE);

                    int fileSize = Integer.parseInt(dis.readLine());
                    Log.d(TAG, "file size: " + fileSize);
                    String filename = dis.readLine();
                    Log.d(TAG, "Filename: " + filename);
                    byte[] bytes = new byte[fileSize];
                    int count  = 0;
                    //dis.read(bytes);
                    dis.readFully(bytes);
                    Log.d(TAG, "byte array done!");
                    builder.setMessage(filename);
                    builder.setData(bytes);
                    builder.setDataSizeInByte(fileSize);


                } else {
                    builder.setType(IO.SEND_MESSAGE);
                    String message = dis.readUTF();
                    //String message = br.readLine();
                    builder.setMessage(message);
                }

                MessagePackage messagePackage = builder.build();
                Log.d(TAG, "Data received: " + messagePackage);
                if (messagePackage.getEvent().equalsIgnoreCase(IO.CLIENT_DISCONNECT)) {
                    socket.disconnectListener.onDisconnect(socket);
                    onDestroy();
                    return null;
                }

                socket.newMessageListener.onNewMessage(socket, messagePackage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*sentence_from_client = "";

        OutputStream os = socket.getSocket().getOutputStream();
        InputStream is = socket.getSocket().getInputStream();
        int control_byte;
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader inFromClient =
                new BufferedReader(reader);

        control_byte = is.read();

        Log.d("SERVER TAG","Control byte: " + control_byte);
        if (control_byte == Server.CLIENT_DISCONNECT || control_byte == -1) {
            onDestroy();
            return null;
        }
        else if (control_byte == Server.SEND_MESSAGE) {
            sentence_from_client = inFromClient.readLine();
            Log.d("SERVER TAG","New Message: " + sentence_from_client);
            socket.listener.handleEvent(Server.SEND_MESSAGE,socket.getSocketId(),sentence_from_client);
        } else if (control_byte == Server.SEND_FILE) {

            String filename = inFromClient.readLine();
            Log.d("SERVER TAG","File receive repairing. File name: " + filename);
            File file = new File(Environment.getExternalStorageDirectory(),filename);
            Log.d("SERVER TAG","File path: " + file.getPath());

            int bytes = 0;
            FileOutputStream fileOutputStream
                    = new FileOutputStream(file);
            DataInputStream dis = new DataInputStream(is);

            long size
                    = dis.readLong(); // read file size
            byte[] buffer = new byte[4 * 1024];
            while (size > 0
                    && (bytes = dis.read(
                    buffer, 0,
                    (int)Math.min(buffer.length, size)))
                    != -1) {
                // Here we write the file using write method
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes; // read upto file size
            }
            // Here we received file
            fileOutputStream.close();
            Log.d("SERVER TAG", "file received!");
            socket.newFileListener.handleFile(file.getPath(),filename);*/
        return null;
    }

    public void onDestroy() {
        try {
            socket.getSocket().close();
            socket.disconnectListener.onDisconnect(socket);
            Log.d("SERVER TAG", "Client disconnected!");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
