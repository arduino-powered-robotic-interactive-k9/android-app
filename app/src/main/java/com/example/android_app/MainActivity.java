package com.example.android_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_ENABLE_BLUETOOTH = 1;
    private static final int REQ_CODE_SEND_VOICE = 2;

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
    protected Button shakeButton;
    protected Button sitButton;
    protected Button heyButton;
    protected ImageButton micButton;

    private TextView sentText;

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
        shakeButton = findViewById(R.id.shake_button);
        sitButton = findViewById(R.id.sit_button);
        heyButton = findViewById(R.id.hey_button);
        connectButton = findViewById(R.id.connect_button);
        micButton = findViewById(R.id.mic_button);

        sentText = findViewById(R.id.sent_text);

        setConnected(false);

        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("stand");
            }
        });

        bowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("bow");
            }
        });

        straightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("straight");
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("down");
            }
        });

        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("walk");
            }
        });

        shakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("shake");
            }
        });

        sitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("sit");
            }
        });

        heyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("hey");
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectButton.setText(R.string.connect_button_connecting);
                if (!connected) {
                    connect();
                } else {
                    disconnect();
                }
            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVoice();
            }
        });
    }

    protected void setConnected(boolean value) {
        connected = value;

        standButton.setEnabled(value);
        bowButton.setEnabled(value);
        straightButton.setEnabled(value);
        downButton.setEnabled(value);
        walkButton.setEnabled(value);
        micButton.setEnabled(value);
        shakeButton.setEnabled(value);
        sitButton.setEnabled(value);
        heyButton.setEnabled(value);

        if (value) {
            standButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));
            bowButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));
            straightButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));
            downButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));
            walkButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));
            shakeButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));
            sitButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));
            heyButton.setTextColor(this.getResources().getColor(R.color.colorTextEnabled));

            micButton.setAlpha(255);
            sentText.setAlpha(1);
        } else {
            standButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));
            bowButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));
            straightButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));
            downButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));
            walkButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));
            shakeButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));
            sitButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));
            heyButton.setTextColor(this.getResources().getColor(R.color.colorTextDisabled));

            micButton.setAlpha(0);
            sentText.setAlpha(0);
            sentText.setText("");
        }
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
            try {
                startActivityForResult(enable, 1);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                connectButton.setText(R.string.connect_button_disconnected);
            }
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

        setConnected(false);
        connectButton.setText(R.string.connect_button_disconnected);
    }

    protected void sendVoice() {
        Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tell me what you want me to do");

        try {
            startActivityForResult(voice, REQ_CODE_SEND_VOICE);
        } catch (ActivityNotFoundException ignore) {}
    }

    protected boolean send(String text) {
        if (!connected) return false;

        byte[] bytes = text.getBytes();

        if (stream == null) {
            Toast.makeText(getBaseContext(), "Something went wrong, please restart app", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            stream.write(bytes);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Could not send, please check MAC Address is correct", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle intent callbacks (currently that is enabling connection and sending voice).
        switch (requestCode) {
            case REQ_CODE_ENABLE_BLUETOOTH: {
                if (resultCode == RESULT_OK) {
                    setupConnection();
                } else {
                    connectButton.setText(R.string.connect_button_disconnected);
                }
                break;
            }

            case REQ_CODE_SEND_VOICE: {
                if (resultCode == RESULT_OK && data != null) {
                     String text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);

                     if (send(text)) {
                         sentText.setText(text);
                     }
                }
                break;
            }
            default: break;
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

        setConnected(true);
        connectButton.setText(R.string.connect_button_connected);
    }
}
