package com.example.smartpot;
//package com.benledmatrix.esp32controloverwifi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartpot.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class HomePage extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Check for Internet permission for SDK 23 or higher
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {

                //rationale to display why the permission is needed
                Toast.makeText(this, "The app needs access to the Internet to send data to the ESP32", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST_CODE);
        }

        // Set up a button to send data when clicked
        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
    }

    private void sendData() {
        // Set up a thread to send the data in the background
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Connect to the ESP32's IP address and port
                    Log.d("Sending Data", "Button Pressed, connecting to ESP32.....");
                    Socket socket = new Socket("192.168.1.7", 80);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());

                    // Send the data
                    out.println("Hello, ESP32!");
                    out.flush();
                    Log.d("Sending Data", "Data Sent!");

                    // Close the connection
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IMPORTANT_LOGGING", "Error Sending Data: "+e.getMessage());
                }
            }
        }).start();
    }
}