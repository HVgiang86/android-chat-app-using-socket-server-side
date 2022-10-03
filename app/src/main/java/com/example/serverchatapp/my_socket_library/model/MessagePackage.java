package com.example.serverchatapp.my_socket_library.model;

import java.io.Serializable;

public class MessagePackage {
    private final String sender;
    private final String event;
    private final boolean isFile;
    private final String message;
    private final byte[] data;
    private final int dataSizeInByte;

    public MessagePackage(String sender, String event, boolean isFile, String message) {
        this.event = event;
        this.isFile = isFile;
        this.message = message;
        data = null;
        dataSizeInByte = 0;
        this.sender = sender;
    }

    public MessagePackage(String sender, String event, boolean isFile, String message, byte[] data, int dataSizeInByte) {
        this.event = event;
        this.isFile = isFile;
        this.data = data;
        this.dataSizeInByte = dataSizeInByte;
        this.message = message;
        this.sender = sender;
    }

    public boolean isFile() {
        return isFile;
    }

    public String getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getData() {
        return data;
    }

    public int getDataSizeInByte() {
        return dataSizeInByte;
    }

    public String getSender() {
        return sender;
    }
}
