package com.example.serverchatapp.socket_server;

import android.util.Log;

import com.example.serverchatapp.my_socket_library.IO;
import com.example.serverchatapp.my_socket_library.model.MessagePackage;
import com.example.serverchatapp.my_socket_library.model.MessagePackageBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Server {
    private final static Server INSTANCE = new Server();
    private static final String TAG = "SERVER TAG";
    List<MySocket> socketList;
    private boolean hasClientConnected = false;
    private ServerSocket serverSocket;
    private String username;

    //Singleton
    private Server() {
    }

    public void createServer(int port) {
        Thread socketServerThread = new Thread(new SocketServerThread(port));
        socketServerThread.start();
        socketList = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static Server getInstance() {
        return INSTANCE;
    }

    public boolean hasClientConnected() {
        return hasClientConnected;
    }

    public void emitMessageAll(String message) {
        for (MySocket socket : socketList) {
            socket.emitMessage(username, message);
        }
    }

    public void emitFileAll(String filePath, String filename) {
        for (MySocket socket : socketList) {
            socket.emitFile(username, filePath, filename);
        }
    }

    public void emitAll(MessagePackage messagePackage) {
        for (MySocket socket : socketList) {
            socket.emit(messagePackage);
        }
    }

    public void emitAllExcept(MySocket socket, MessagePackage messagePackage) {
        for (MySocket socket1 : socketList) {
            if (socket1.getSocketId() != socket.getSocketId()) {
                socket1.emit(messagePackage);
            }
        }
    }

    public static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = ip + "Server running at : " + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e + "\n";
        }

        if (ip.length()>47) {
            int i = ip.indexOf("Server",5);
            ip = Server.getIpAddress().substring(0,i);
        }
        return ip;
    }

    public void onDestroy() {
        MessagePackageBuilder builder = new MessagePackageBuilder();
        builder.setEvent(IO.CLIENT_DISCONNECT);
        builder.setEvent(IO.SEND_MESSAGE);
        builder.setMessage("Disconnect");

        emitAll(builder.build());

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {
        private final int port;

        public SocketServerThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                Log.d(TAG, "CURRENT IP: " + getIpAddress());
                // create ServerSocket using specified port
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(port));


                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();
                    hasClientConnected = true;

                    MySocket mySocket = new MySocket(socketList.size());
                    mySocket.setSocket(socket);

                    Log.d(TAG, "Client accepted: " + "Socket Id: " + mySocket.getSocketId() + " Socket IP: " + socket.getInetAddress()
                            + " Port: " + socket.getPort());

                    socketList.add(mySocket);
                    mySocket.setNewMessageListener(new OnNewMessageListener());
                    mySocket.setDisconnectListener((socket1) -> socket1.onDestroy());
                    mySocket.startReadingStream();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}