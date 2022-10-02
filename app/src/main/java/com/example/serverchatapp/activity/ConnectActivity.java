package com.example.serverchatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serverchatapp.R;
import com.example.serverchatapp.socket_server.Server;

public class ConnectActivity extends AppCompatActivity {
    private Server server;
    private TextView ipServerTv;
    private EditText portServerEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ipServerTv = findViewById(R.id.server_ip);
        portServerEdt = findViewById(R.id.server_port);

        server = Server.getInstance();
        int i = server.getIpAddress().indexOf("Server",5);
        String ipServer = server.getIpAddress().substring(0,i);
        ipServerTv.setText(ipServer);
    }

    public void createServer(View v) {
        String portStr = portServerEdt.getText().toString().trim();
        if (portStr.length() == 0)
            return;

        int port = Integer.parseInt(portStr);
        server.createServer(port);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}