package com.example.android_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    protected static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    protected static final String MAC_ADDRESS = "98:D3:51:F9:4C:83";

    protected BluetoothAdapter adapter;
    protected BluetoothSocket socket;
    protected OutputStream stream;

    protected Button standButton;
    protected Button bowButton;
    protected Button straightButton;
    protected Button downButton;
    protected Button walkButton;
    protected Button connectButton;

    protected boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        standButton = findViewById(R.id.stand_button);
        bowButton = findViewById(R.id.bow_button);
        straightButton = findViewById(R.id.straight_button);
        downButton = findViewById(R.id.down_button);
        walkButton = findViewById(R.id.walk_button);
        connectButton = findViewById(R.id.connect_button);

        connected = false;

        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectButton.setText(R.string.connect_button_connecting);

                if (!connected) {
                    connect();
                } else {
                    disconnect();
                }
            }
        });
    }

    protected void connect() {
        adapter = BluetoothAdapter.getDefaultAdapter();

        // Device does not support bluetooth.
        if (adapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
            connectButton.setText(R.string.connect_button_disconnected);
            return;
        }

        // Enable bluetooth.
        if (!this.adapter.isEnabled()) {
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable, 1);
        } else {
            // Bluetooth already enabled.
            setupConnection();
        }
    }

    protected void disconnect() {
        try {
            stream.flush();
            socket.close();
        } catch (Exception e) {
            // Not sure if the thing stays connected or not. Lets just hope this never happens.
            Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

        connectButton.setText(R.string.connect_button_disconnected);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Connect to device.
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setupConnection();
        } else {
            connectButton.setText(R.string.connect_button_disconnected);
        }
    }

    protected void setupConnection() {
        BluetoothDevice device = adapter.getRemoteDevice(MAC_ADDRESS);

        this.adapter.cancelDiscovery();
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            socket = (BluetoothSocket) m.invoke(device, MY_UUID);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Failed to connect to device", Toast.LENGTH_LONG).show();
            connectButton.setText(R.string.connect_button_disconnected);
            return;
        }

        try {
            socket.connect();
        } catch (Exception e) {
            try {
                socket.close();
            } catch (Exception ignored) {}

            Toast.makeText(getBaseContext(), "Failed to connect to device", Toast.LENGTH_LONG).show();
            connectButton.setText(R.string.connect_button_disconnected);
            return;
        }

        try {
            stream = socket.getOutputStream();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Failed to create stream to device", Toast.LENGTH_LONG).show();
            connectButton.setText(R.string.connect_button_disconnected);
            return;
        }

        connectButton.setText(R.string.connect_button_connected);
    }
}
