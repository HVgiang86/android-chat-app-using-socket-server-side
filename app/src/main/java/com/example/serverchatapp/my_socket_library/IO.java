package com.example.serverchatapp.my_socket_library;

import com.example.serverchatapp.my_socket_library.model.MessagePackage;

import java.net.Socket;

public interface IO {
    String SEND_MESSAGE = "send_message";
    String SEND_FILE = "send_file";
    String CLIENT_DISCONNECT = "disconnect";
    interface OnConnectListener{
        void onConnect(SingleSocket socket);
    }

    interface OnDisconnectListener{
        void onDisconnect(SingleSocket socket);
    }

    interface OnNewMessageListener{
        void onNewMessage(SingleSocket socket, MessagePackage messagePackage);
    }
}
