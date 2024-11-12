package vendor.datalogic.service.eventprofiles;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.net.NetworkRequest.Builder;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.datalogic.device.PersistenceType;
import com.datalogic.device.configuration.ConfigException;
import com.datalogic.device.configuration.ProfileManager;
import com.datalogic.device.configuration.ProfileType;
import com.datalogic.device.configuration.PropertyID;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = "EventProfiles"; // Tag for logging
    private String profileName = "wifi_test.json";
    private ConnectivityManager connectivityManager; // Manages network connectivity
    private ConnectivityManager.NetworkCallback networkCallback; // Callback for network events
    private ProfileManager pm; // Manages device profiles
    private HashMap map; // Stores profile properties

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate is called");
        setContentView(R.layout.activity_main);

        // 1. Setting up Wi-Fi connectivity to notify when Access Point changes
        setUp();

        // 2. Create a new instance of ProfileManager with the current context
        pm = new ProfileManager(this);

        // 3. Create a HashMap and add key-value pairs for profile properties
        map = new HashMap();
        map.put(PropertyID.GREEN_SPOT_ENABLE, "false");  // Disable green spot (BooleanProperty)
        map.put(PropertyID.DEVICE_NAME_BASE, "wifi");    // Set device name base to "wifi" (TextProperty)

        // 4. Create a profile using the ProfileManager
        // Step 4.1: Check if "wifi_test.json" profile already exists
        boolean profileExists = false;
        for (ProfileType name : pm.getProfilesList()) {
            if (profileName.equals(name.name)) {
                profileExists = true;
                break;
            }
        }

        // Step 4.2: If not exists, create it
        if (!profileExists) {
            pm.createProfile(
                    profileName, // Profile file name
                    map,              // Profile properties
                    "Wifi Test Profile", // Profile description
                    PersistenceType.ENTERPRISE_RESET_PERSISTENT // Persistent across device reboots
            );
        }

        // Note:
        // - When Wi-Fi connection is ready, the profile "wifi_test.json" will be loaded (see onAvailable).
        // - When Wi-Fi connection is lost, the profile "wifi_test.json" will be unloaded (see onLost).
    }

    private void setUp() {
        // Initialize ConnectivityManager to monitor network changes
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for location permission (required for Wi-Fi scanning on API 23+)
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, register network callback
            registerNetworkCallback();
        } else {
            // Request location permission from the user
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void registerNetworkCallback() {
        Log.d(TAG, "registerNetworkCallback: Callback registered");
        // Define a network callback to handle network events
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.d(TAG, "onAvailable: Network is available");
                // Called when a network becomes available
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);

                // Check if the available network is Wi-Fi
                if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Toast.makeText(MainActivity.this, "Connected to Wi-Fi", Toast.LENGTH_SHORT).show();
                    // Load the Wi-Fi profile
                    pm.loadProfile(profileName);
                }
            }

            @Override
            public void onLost(Network network) {
                Log.d(TAG, "onLost: Network is lost");
                // Called when a network is lost
                Toast.makeText(MainActivity.this, "Network lost", Toast.LENGTH_SHORT).show();
                // Unload the Wi-Fi profile
                pm.unloadProfile();
            }
        };

        // Build a network request to listen for Wi-Fi connectivity changes
        NetworkRequest.Builder requestBuilder = new NetworkRequest.Builder();
        requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI); // Listen only for Wi-Fi
        connectivityManager.registerNetworkCallback(requestBuilder.build(), networkCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy is called");

        // Unregister the network callback to avoid memory leaks
        if (networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }

        // Delete the profile when the activity destroys
        pm.deleteProfile(profileName);
    }
}