package com.datalogic.apreconnectionsampleapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.datalogic.device.wifi.WifiManager;
import com.datalogic.device.wifi.WifiProfile;


public class MainActivity extends Activity {
    private Button createProfileButton, connectionButton, wifiInfoButton;
    private EditText wifiSsidNameEditText, wifiSsisPasswordEditText;
    private TextView wifiProfileInfo;
    private String stringWifiSSIDName, stringWifiSSIDPassword;
    private final static int DATA_SSID = 0;
    private final static int DATA_PASSWORD = 1;
    public WifiManager wifiManager;
    public WifiProfile wifiProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = new WifiManager();
        setUpView();
        wifiProfile = createWifiProfile();
    }

    private String[] getEditTextData() {
        wifiSsidNameEditText = findViewById(R.id.ssid_name);
        stringWifiSSIDName = wifiSsidNameEditText.getText().toString();
        wifiSsisPasswordEditText = findViewById(R.id.ssid_password);
        stringWifiSSIDPassword = wifiSsisPasswordEditText.getText().toString();
        return new String[] {stringWifiSSIDName, stringWifiSSIDPassword};
    }

    private void setUpView() {
        createProfileButton = findViewById(R.id.create_profile_button);
        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiProfile = createWifiProfile();
                createProfileStatusNotification(wifiProfile);
                showWifiInfo(wifiProfile);
            }
        });

        connectionButton = findViewById(R.id.connect_button);
        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToNetwork(wifiProfile);
                showWifiInfo(wifiProfile);
            }
        });

        wifiInfoButton = findViewById(R.id.disconnect_button);
        wifiInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectToNetwork(wifiProfile);
                showWifiInfo(wifiProfile);
            }
        });
        wifiProfileInfo = findViewById(R.id.profile_information);
        showWifiInfo(wifiProfile);
    }

    private WifiProfile createWifiProfile() {
        String[] data = getEditTextData();
        WifiProfile.Builder profileBuilder = wifiManager.profileBuilder(data[DATA_SSID]);
        if (!data[DATA_PASSWORD].isEmpty()) {
            profileBuilder.withWpaPsk(data[DATA_PASSWORD]);
        }
        else {
            profileBuilder.withOpen();
        }
        return profileBuilder.build();
    }

    private void connectToNetwork(WifiProfile profile) {
        boolean connected = profile.connect();
        if (connected) {
            Toast.makeText(this, "Wi-Fi is connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to connect to Wi-Fi", Toast.LENGTH_SHORT).show();
        }
    }

    private void disconnectToNetwork(WifiProfile profile) {
        boolean disconnected = profile.disconnect();
        if (disconnected) {
            Toast.makeText(this, "Wi-Fi is disconnected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to disconnected Wi-Fi", Toast.LENGTH_SHORT).show();
        }
    }

    private void createProfileStatusNotification (WifiProfile profile) {
        if(profile.exists()) {
            Toast.makeText(this, "Creating WifiProfile sucessfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Creating WifiProfile failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showWifiInfo(WifiProfile profile) {
        try {
            String connectionStatus = (wifiProfile.isConnected() ? getResources().getString(R.string.wifi_connected) : getResources().getString(R.string.wifi_disconnected));
            wifiProfileInfo.setText(String.format("Wifi Profile create for SSID:%s\nStatus: %s", wifiSsidNameEditText.getText().toString(), connectionStatus));
            if(!profile.exists()) {
                wifiProfileInfo.setText(R.string.wifi_waiting);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}



