package com.example.smartpot;
//package com.benledmatrix.esp32controloverwifi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HomePage extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    private void sendData() {
        // Set up a thread to send the data in the background
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Connect to the ESP32's IP address and port
                    Log.d("Sending Data", "Button Pressed, connecting to ESP32.....");
//                    InetAddress iAddress = InetAddress.getLocalHost();
//                    String server_ip = iAddress.getHostAddress();
//                    Log.d("IMPORTANT_LOGGING", server_ip);

                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);

                    Log.d("IMPORTANT_LOGGING", ipString);

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    for (int i = 0; i < 255; i++) {
                        String testIp = prefix + i;

                        InetAddress address = InetAddress.getByName(testIp);
                        boolean reachable = address.isReachable(1000);
                        String hostName = address.getCanonicalHostName();

                        if (reachable)
                            Log.i("IMPORTANT_LOGGING", "Host: " + hostName + " (" + testIp + " ) is reachable!");
                    }

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