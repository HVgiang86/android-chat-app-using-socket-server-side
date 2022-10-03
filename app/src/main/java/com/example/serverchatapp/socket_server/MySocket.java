package com.example.serverchatapp.socket_server;

import com.example.serverchatapp.my_socket_library.IO;
import com.example.serverchatapp.my_socket_library.SingleSocket;
import com.example.serverchatapp.my_socket_library.model.MessagePackageBuilder;

import java.io.File;
import java.io.FileNotFoundException;

public class MySocket extends SingleSocket {
    private int socketId;

    public MySocket(int socketId) {
        this.socketId = socketId;
    }

    public MySocket() {
    }

    public void emitMessage(String username, String message) {
        MessagePackageBuilder builder = new MessagePackageBuilder();
        builder.setEvent(IO.SEND_MESSAGE);
        builder.setSender(username);
        builder.setType(IO.SEND_MESSAGE);
        builder.setMessage(message);
        emit(builder.build());
    }

    public void emitFile(String username, String filePath, String filename) {
        File file = new File(filePath);
        MessagePackageBuilder builder = new MessagePackageBuilder();
        builder.setMessage(filename);
        builder.setEvent(IO.SEND_FILE);
        builder.setType(IO.SEND_FILE);
        builder.setSender(username);
        try {
            builder.setDataFromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        emit(builder.build());
    }

    public int getSocketId() {
        return socketId;
    }
}
