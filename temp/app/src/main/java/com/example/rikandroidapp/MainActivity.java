// Credit to http://solderer.tv/data-transfer-between-android-and-arduino-via-bluetooth/

package com.example.rikandroidapp;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Bluetooth variables.
    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private OutputStream stream;

    // MAC address of device.
    private static String address = "98:D3:51:F9:4C:83";

    // No need to change the following variable.
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * This runs once app is opened.
     * @param savedInstanceState system generated state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Allows app to load. Don't touch this.
        setContentView(R.layout.activity_main);

        // Setting buttons from display to interact with.
//        Button standButton = findViewById(R.id.standButton);
//        Button sitButton = findViewById(R.id.sitButton);
//
//        // Set up bluetooth adapter.
//        this.adapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (adapter != null) {
//            /*
//             * Prompt user to turn on bluetooth if bluetooth is not enabled.
//             * If user turns on bluetooth, then the onResume function will run.
//             * If bluetooth was already enabled, then onResume function will run.
//             */
//            if (!this.adapter.isEnabled()) {
//                Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enable, 1);
//            }
//        } else {
//            Toast.makeText(getBaseContext(), "Bluetooth is not supported.", Toast.LENGTH_SHORT).show();
//        }
//
//        // Setting up callbacks when buttons are clicked.
//        standButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Sending stand to RIK", Toast.LENGTH_SHORT).show();
//                sendData("stand");
//            }
//        });
//
//        sitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Sending sit to RIK", Toast.LENGTH_SHORT).show();
//                sendData("sit");
//            }
//        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (this.adapter == null) return;
//
//        // Obtains device given MAC address.
//        BluetoothDevice device = this.adapter.getRemoteDevice(address);
//
//        // Creates socket to that device.
//        this.socket = createBluetoothSocket(device);
//        this.adapter.cancelDiscovery();
//        if (this.socket == null) {
//            return;
//        }
//
//        // Connects to device using the socket.
//        Toast.makeText(getBaseContext(), "Connecting to device.", Toast.LENGTH_SHORT).show();
//        try {
//            this.socket.connect();
//        } catch (IOException eOpen) {
//            Toast.makeText(getBaseContext(), "Failed to connect to device.", Toast.LENGTH_SHORT).show();
//            try {
//                this.socket.close();
//            } catch(IOException eClose) {
//                Toast.makeText(getBaseContext(), "Failed to close socket.", Toast.LENGTH_SHORT).show();
//            }
//            return;
//        }
//
//        // Creates output stream to send data to device.
//        try {
//            this.stream = socket.getOutputStream();
//        } catch (IOException e) {
//            Toast.makeText(getBaseContext(), "Failed to create output stream.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        /*
//         * Closes output stream and socket connection, as application is paused.
//         * Application will connect again when onResume function is triggered.
//         */
//        if (this.stream != null) {
//            try {
//                this.stream.flush();
//            } catch (IOException e) {
//                return;
//            }
//        }
//
//        try {
//            this.socket.close();
//        } catch (Exception e) {
//        }
//    }
//
//    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) {
//        try {
//            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
//            return (BluetoothSocket) m.invoke(device, MY_UUID);
//        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), "Failed to open socket to device.", Toast.LENGTH_SHORT).show();
//        }
//
//        return null;
//    }
//
//    private void sendData(String message) {
//        byte[] bytes = message.getBytes();
//
//        if (this.stream == null) {
//            Toast.makeText(getBaseContext(), "Failed to send message to device.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            this.stream.write(bytes);
//        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), "Failed to send message to device.", Toast.LENGTH_SHORT).show();
//            if (address.equals("00:00:00:00:00:00")) {
//                Toast.makeText(getBaseContext(), "MAC address is not set up.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
