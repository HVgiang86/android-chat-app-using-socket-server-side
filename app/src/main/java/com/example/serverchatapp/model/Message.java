package com.example.serverchatapp.model;

public class Message {
    private String content;
    private boolean isServer;
    private boolean isFile;

    public Message(String content, boolean isServer, boolean isFile) {
        this.content = content;
        this.isServer = isServer;
        this.isFile = isFile;
    }

    public Message() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
