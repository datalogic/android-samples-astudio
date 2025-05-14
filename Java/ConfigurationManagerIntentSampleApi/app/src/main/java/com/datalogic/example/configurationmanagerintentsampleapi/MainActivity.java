package com.datalogic.example.configurationmanagerintentsampleapi; // Or your desired package

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

// Datalogic SDK imports
import com.datalogic.device.Intents; // Defines Datalogic specific intent actions and extras
import com.datalogic.device.configuration.PropertyID;     // Contains unique identifiers for device properties
import com.datalogic.device.configuration.DeviceNameSuffix; // Enum for DEVICE_NAME_SUFFIX
import com.datalogic.device.configuration.StringSetBlob; // Specific Blob type for VIRTUAL_KEYBOARDS_CUSTOM_ENABLED

import java.util.HashMap;
import java.util.Map; // Used for iterating over map entries

/**
 * MainActivity is an example demonstrating how to use the Datalogic SDK's Intent interface
 * ({@link com.datalogic.device.Intents#ACTION_CONFIGURATION_COMMIT}) to request changes
 * to various device properties.
 *
 * <p>This sample showcases:</p>
 * <ul>
 * <li>Preparing a {@link HashMap} of property changes (PropertyID to String value).</li>
 * <li>Handling various property types for intent-based commit:
 * <ul>
 * <li>Boolean (e.g., {@code PropertyID.LONG_PRESS_HOME_BUTTON_ENABLED})</li>
 * <li>Numeric (e.g., {@code PropertyID.SCREEN_BRIGHTNESS})</li>
 * <li>Text (e.g., {@code PropertyID.DEVICE_NAME_BASE})</li>
 * <li>Enum (e.g., {@code PropertyID.DEVICE_NAME_SUFFIX})</li>
 * <li>Blob (e.g., {@code PropertyID.VIRTUAL_KEYBOARDS_CUSTOM_ENABLED})</li>
 * </ul>
 * </li>
 * <li>Sending an {@link Intents#ACTION_CONFIGURATION_COMMIT} broadcast.</li>
 * <li>Registering a {@link BroadcastReceiver} to listen for {@link Intents#ACTION_CONFIGURATION_CHANGED}
 * and {@link Intents#ACTION_CONFIGURATION_BOOT_REQUIRED}.</li>
 * </ul>
 * <p>
 * For more detailed information, refer to the official Datalogic Android SDK documentation.
 * </p>
 */
public class MainActivity extends Activity {
    private static final String TAG = "IntentConfigSample";
    private BroadcastReceiver configurationChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ===========================================================
        // STEP 1: REGISTER BROADCAST RECEIVER
        // ===========================================================
        Log.d(TAG, "Registering broadcast receiver for configuration changes.");
        registerConfigurationChangeReceiver();

        // ===========================================================
        // STEP 2: PREPARE PROPERTIES FOR INTENT COMMIT
        // ===========================================================
        Log.d(TAG, "Preparing properties for ACTION_CONFIGURATION_COMMIT intent.");
        HashMap<Integer, String> propertiesToChange = new HashMap<>();

        // --- 2.1. Boolean Property: PropertyID.LONG_PRESS_HOME_BUTTON_ENABLED ---
        String newValue = Boolean.toString(true); // Toggle
        propertiesToChange.put(PropertyID.LONG_PRESS_HOME_BUTTON_ENABLED, newValue);
        Log.d(TAG, "Preparing to set LONG_PRESS_HOME_BUTTON_ENABLED to: " + newValue);

        // --- 2.2. Numeric Property: PropertyID.SCREEN_BRIGHTNESS ---
        String newBrightnessLevel = "150"; // Example new brightness
        propertiesToChange.put(PropertyID.SCREEN_BRIGHTNESS, newBrightnessLevel);
        Log.d(TAG, "Preparing to set SCREEN_BRIGHTNESS to: " + newBrightnessLevel);

        // --- 3.3. Text Property: PropertyID.DEVICE_NAME_BASE ---
        String newDeviceNameBase = "DeviceBaseNameExample";
        propertiesToChange.put(PropertyID.DEVICE_NAME_BASE, newDeviceNameBase);
        Log.d(TAG, "Preparing to set DEVICE_NAME_BASE to: \"" + newDeviceNameBase + "\"");

        // --- 3.4. Enum Property: PropertyID.DEVICE_NAME_SUFFIX ---
        DeviceNameSuffix newSuffix = DeviceNameSuffix.SERIAL_NUMBER;
        String newSuffixStr = newSuffix.toString();
        propertiesToChange.put(PropertyID.DEVICE_NAME_SUFFIX, newSuffixStr);
        Log.d(TAG, "Preparing to set DEVICE_NAME_SUFFIX to: " + newSuffixStr);

        // --- 3.5. BlobProperty (StringSetBlob): PropertyID.VIRTUAL_KEYBOARDS_CUSTOM_ENABLED ---
        StringSetBlob newValueToSet = new StringSetBlob();
        newValueToSet.add("com.google.android.tts/com.google.android.apps.speech.tts.googletts.settings.asr.voiceime.VoiceInputMethodService");
        newValueToSet.add("com.another.keyboard/.AnotherIME");  // Add another value
        String stringValueForKeyboards = String.join(",", newValueToSet.toString());
        propertiesToChange.put(PropertyID.VIRTUAL_KEYBOARDS_CUSTOM_ENABLED, stringValueForKeyboards);
        Log.d(TAG, "Preparing to set VIRTUAL_KEYBOARDS_CUSTOM_ENABLED to (comma-separated string): \"" + stringValueForKeyboards + "\"");

        // ===========================================================
        // STEP 4: SEND CONFIGURATION COMMIT INTENT
        // ===========================================================
        Log.d(TAG, "Sending ACTION_CONFIGURATION_COMMIT intent with properties: " + propertiesToChange.toString());
        Intent commitIntent = new Intent(Intents.ACTION_CONFIGURATION_COMMIT);
        commitIntent.putExtra(Intents.EXTRA_CONFIGURATION_CHANGED_MAP, propertiesToChange);
        sendBroadcast(commitIntent);
    }

    /**
     * Registers a {@link BroadcastReceiver} to listen for specific Datalogic system intents
     * related to device configuration changes. This allows the application to be notified
     * asynchronously about the outcome of configuration requests or other system-initiated changes.
     *
     * The receiver is set up to listen for:
     * <ul>
     * <li>{@link Intents#ACTION_CONFIGURATION_CHANGED}: Indicates that one or more
     * device configuration properties have changed. The intent carries details about
     * which properties were successfully changed and which ones failed (if any).</li>
     * <li>{@link Intents#ACTION_CONFIGURATION_BOOT_REQUIRED}: Signals that a device
     * reboot is necessary for some pending configuration changes to take full effect.</li>
     * </ul>
     * This method should be called, for example, in {@code onCreate()} of an Activity, and
     * the receiver should be unregistered in {@code onDestroy()} to prevent leaks.
     */
    private void registerConfigurationChangeReceiver() {
        // Create a new instance of the BroadcastReceiver.
        configurationChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // ===========================================================
                // RECEIVE A CONFIGURATION CHANGED OR REBOOT REQUIRED
                // ===========================================================
                String action = intent.getAction();
                Log.d(TAG, "Received intent with action: " + action);

                // Check if the received intent is for a configuration change.
                if (Intents.ACTION_CONFIGURATION_CHANGED.equals(action)) {
                    Log.i(TAG, "ACTION_CONFIGURATION_CHANGED received.");

                    // Extract the map of successfully changed properties.
                    // The key is the PropertyID (Integer), and the value is the new property value (String).
                    @SuppressWarnings("unchecked") // Suppress warning for HashMap cast, as this is the expected type.
                    HashMap<Integer, String> changedMap = (HashMap<Integer, String>) intent.getSerializableExtra(Intents.EXTRA_CONFIGURATION_CHANGED_MAP);
                    if (changedMap != null && !changedMap.isEmpty()) {
                        Log.d(TAG, "Successfully changed properties reported by broadcast:");
                        for (Map.Entry<Integer, String> entry : changedMap.entrySet()) {
                            Log.d(TAG, "  PropertyID " + entry.getKey() + " set to: " + entry.getValue());
                        }
                    } else {
                        Log.d(TAG, "No successfully changed properties in EXTRA_CONFIGURATION_CHANGED_MAP from broadcast.");
                    }

                    // Extract the map of properties that failed to change (errors).
                    // The key is the PropertyID (Integer), and the value is the property value that failed to be applied (String).
                    @SuppressWarnings("unchecked") // Suppress warning for HashMap cast, as this is the expected type.
                    HashMap<Integer, String> errorMap = (HashMap<Integer, String>) intent.getSerializableExtra(Intents.EXTRA_CONFIGURATION_ERROR_MAP);
                    if (errorMap != null && !errorMap.isEmpty()) {
                        Log.w(TAG, "Properties that failed to change (errors) reported by broadcast:");
                        for (Map.Entry<Integer, String> entry : errorMap.entrySet()) {
                            Log.w(TAG, "  PropertyID " + entry.getKey() + " failed to set to value: " + entry.getValue());
                        }
                    } else {
                        Log.d(TAG, "No errors reported in EXTRA_CONFIGURATION_ERROR_MAP from broadcast.");
                    }

                // Check if the received intent indicates that a device reboot is required.
                } else if (Intents.ACTION_CONFIGURATION_BOOT_REQUIRED.equals(action)) {
                    Log.w(TAG, "ACTION_CONFIGURATION_BOOT_REQUIRED received. Device reboot needed to apply all changes.");
                }
            }
        };

        // Create an IntentFilter to specify which intent actions this receiver should respond to.
        // This ensures the receiver only gets intents it's interested in.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intents.ACTION_CONFIGURATION_CHANGED); // Add action for when device configuration properties have changed.
        intentFilter.addAction(Intents.ACTION_CONFIGURATION_BOOT_REQUIRED); // // Add action for when a reboot is required to apply configuration changes.
        // Register the BroadcastReceiver with the system, associating it with the defined IntentFilter.
        // This receiver is context-registered and will be active as long as the registering context (e.g., Activity) is valid.
        registerReceiver(configurationChangeReceiver, intentFilter);
        Log.d(TAG, "BroadcastReceiver registered for Datalogic configuration intents.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ===========================================================
        // UNREGISTER BROADCAST RECEIVER
        // ===========================================================
        // It's crucial to unregister the receiver when it's no longer needed (e.g., when the Activity is destroyed)
        // to prevent memory leaks and ensure proper application behavior.
        if (configurationChangeReceiver != null) {
            unregisterReceiver(configurationChangeReceiver);
            Log.d(TAG, "BroadcastReceiver unregistered.");
            configurationChangeReceiver = null;
        }
        Log.d(TAG, "onDestroy completed.");
    }
}