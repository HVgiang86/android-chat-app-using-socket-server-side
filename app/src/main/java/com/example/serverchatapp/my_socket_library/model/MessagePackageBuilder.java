package com.example.serverchatapp.my_socket_library.model;

import com.example.serverchatapp.my_socket_library.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MessagePackageBuilder {
    public final static int MAX_FILE_SIZE_IN_BYTE = 50 * 1024 * 1024;
    private String event;
    private boolean isFile;
    private String message;
    private byte[] data = null;
    private int dataSizeInByte;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setType(String type) {
        isFile = type.equalsIgnoreCase("file") || type.equalsIgnoreCase(IO.SEND_FILE);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setDataSizeInByte(int dataSizeInByte) {
        this.dataSizeInByte = dataSizeInByte;
    }

    public boolean setDataFromFile(File file) throws FileNotFoundException {
        dataSizeInByte = (int) file.length();
        message = file.getName();
        if (dataSizeInByte > MAX_FILE_SIZE_IN_BYTE) {
            throw new FileNotFoundException("File size > 50Mb");
        }

        try {
            data = new byte[(int) dataSizeInByte];
            FileInputStream fis = new FileInputStream(file);
            fis.read(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public MessagePackage build() {
        if (isFile) {
            return new MessagePackage(event, true, message, data, dataSizeInByte);
        }
        return new MessagePackage(event, false, message);
    }
}
