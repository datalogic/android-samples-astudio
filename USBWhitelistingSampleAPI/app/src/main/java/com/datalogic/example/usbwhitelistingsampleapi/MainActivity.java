package com.datalogic.example.usbwhitelistingsampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.datalogic.device.configuration.ConfigurationManager;
import com.datalogic.device.configuration.PropertyID;
import com.datalogic.device.configuration.BlobProperty;
import com.datalogic.device.configuration.UsbHostWhitelisting;
import com.datalogic.device.configuration.UsbWhitelistedDevice;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * MainActivity is an example of how to use the Datalogic SDK to configure USB Host Whitelisting.
 * <p>
 * This feature allows specific USB devices to be whitelisted for connection, enhancing security
 * and control in enterprise environments. It is particularly useful in kiosk modes or
 * scenarios where device connectivity must be limited to a pre-approved set of peripherals.
 * <p>
 * Links to the Datalogic SDK documentation for the relevant properties and classes:
 * <ul>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html#USB_HOST_WHITELISTING">USB_HOST_WHITELISTING</a></li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/UsbHostWhitelisting.html">UsbHostWhitelisting Class</a></li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/UsbWhitelistedDevice.html">UsbWhitelistedDevice Class</a></li>
 * </ul>
 * </p>
 */
public class MainActivity extends Activity {

    private static final String TAG = "Datalogic USB Whitelist Sample";

    // ====
    // NOTE: THIS SECTION CAN BE MODIFIED TO TEST WITH SPECIFIC DEVICES
    // In this section, you can replace the sample values with the actual USB devices
    // (Vendor ID, Product ID) that the developer has on hand to test the USB Whitelisting configuration.
    // ====
    // Example VID/PID - Replace with actual device VIDs and PIDs
    private static final int WHITELISTED_VID_1 = 0x04FB; // Example Vendor ID
    private static final int WHITELISTED_PID_1 = 0x96A2; // Example Product ID
    private static final String WHITELISTED_NAME_1 = "Approved USB Mouse";
    private static final String WHITELISTED_DESC_1 = "Standard USB mouse";

    // Using String (hex) for VID/PID constructor for UsbWhitelistedDevice
    private static final String WHITELISTED_VID_2_HEX = "05FB"; // Example Vendor ID (Hex)
    private static final String WHITELISTED_PID_2_HEX = "26A2"; // Example Product ID (Hex)
    private static final String WHITELISTED_NAME_2 = "Approved USB Keyboard";
    private static final String WHITELISTED_DESC_2 = "Standard USB keyboard";

    // Example of a device NOT to be whitelisted (for testing removal or checking)
    private static final int NOT_WHITELISTED_VID = 0xAAAA;
    private static final int NOT_WHITELISTED_PID = 0xBBBB;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

        Log.d(TAG, "Starting USB Whitelisting Configuration Sample");

        // ==================================================================
        // STEP 1: INITIALIZING THE CONFIGURATION MANAGER
        // ==================================================================
        // The ConfigurationManager allows interaction with the device's configuration properties.
        ConfigurationManager configManager = new ConfigurationManager(getApplicationContext());

        // ==================================================================
        // STEP 2: CONFIGURING THE USB WHITELIST
        // ==================================================================
        try {
            // Retrieve the BlobProperty for USB host whitelisting.
            BlobProperty usbWhitelistProp = (BlobProperty) configManager.getPropertyById(PropertyID.USB_HOST_WHITELISTING);

            // Get the current UsbHostWhitelisting object or create a new one if null.
            UsbHostWhitelisting usbHostWhitelist;
            if (usbWhitelistProp.get() != null) {
                usbHostWhitelist = (UsbHostWhitelisting) usbWhitelistProp.get();
            } else {
                usbHostWhitelist = new UsbHostWhitelisting();
            }

            // Optional: Clear existing whitelist before adding new devices
             usbHostWhitelist.clear();
             Log.d(TAG, "Cleared existing USB whitelist.");

            // Create UsbWhitelistedDevice objects
            // Constructor using int VID/PID
            UsbWhitelistedDevice device1 = new UsbWhitelistedDevice(
                    WHITELISTED_VID_1,
                    WHITELISTED_PID_1,
                    WHITELISTED_NAME_1,
                    WHITELISTED_DESC_1
            );

            // Constructor using String (hex) VID/PID
            UsbWhitelistedDevice device2 = new UsbWhitelistedDevice(
                    WHITELISTED_VID_2_HEX,
                    WHITELISTED_PID_2_HEX,
                    WHITELISTED_NAME_2,
                    WHITELISTED_DESC_2
            );

            // Add devices to the whitelist
            usbHostWhitelist.add(device1);
            Log.d(TAG, "Added device by VID/PID: " + device1.getVidHex() + "/" + device1.getPidHex() + " (" + device1.getName() + ")");

            List<UsbWhitelistedDevice> deviceList = new ArrayList<>();
            deviceList.add(device2);
            usbHostWhitelist.add(deviceList);
            Log.d(TAG, "Added device via list: " + device2.getVidHex() + "/" + device2.getPidHex() + " (" + device2.getName() + ")");


            // Set the modified UsbHostWhitelisting object back to the property
            usbWhitelistProp.set(usbHostWhitelist);
            Log.d(TAG, "USB Whitelist prepared with " + usbHostWhitelist.getWhitelistedDevices().size() + " rules."); // [cite: 99, 105]

            // ==================================================================
            // STEP 3: COMMITTING THE CHANGES TO APPLY THE CONFIGURATION
            // ==================================================================
            // This saves and applies the changes to the device configuration.
            configManager.commit();
            Log.d(TAG, "USB Whitelist configuration committed successfully.");

            // ==================================================================
            // STEP 4: VERIFYING THE CONFIGURATION
            // ==================================================================
            // Retrieve the whitelist again to verify
            BlobProperty verificationProp = (BlobProperty) configManager.getPropertyById(PropertyID.USB_HOST_WHITELISTING);
            UsbHostWhitelisting currentWhitelist = (UsbHostWhitelisting) verificationProp.get();

            if (currentWhitelist != null) {
                UsbWhitelistedDevice retrievedDevice1 = currentWhitelist.get(WHITELISTED_VID_1, WHITELISTED_PID_1);
                if (retrievedDevice1 != null) {
                    Log.d(TAG, "Verification: Device 1 (" + retrievedDevice1.getName() + ") found in whitelist.");
                } else {
                    Log.e(TAG, "Verification ERROR: Device 1 not found in whitelist.");
                }

                UsbWhitelistedDevice retrievedDevice2 = currentWhitelist.get(Integer.parseInt(WHITELISTED_VID_2_HEX, 16), Integer.parseInt(WHITELISTED_PID_2_HEX, 16));
                if (retrievedDevice2 != null) {
                    Log.d(TAG, "Verification: Device 2 (" + retrievedDevice2.getName() + " - " + retrievedDevice2.getDescription() + ") found in whitelist.");
                } else {
                    Log.e(TAG, "Verification ERROR: Device 2 not found in whitelist.");
                }

                // Example of checking a non-whitelisted device
                UsbWhitelistedDevice nonExistentDevice = currentWhitelist.get(NOT_WHITELISTED_VID, NOT_WHITELISTED_PID);
                if (nonExistentDevice == null) {
                    Log.d(TAG, "Verification: Non-whitelisted device (VID:" + NOT_WHITELISTED_VID + "/PID:" + NOT_WHITELISTED_PID + ") correctly not found.");
                } else {
                    Log.e(TAG, "Verification ERROR: Non-whitelisted device found in whitelist.");
                }

                // Listing all whitelisted devices
                List<UsbWhitelistedDevice> allDevices = currentWhitelist.getWhitelistedDevices();
                Log.d(TAG, "Current Whitelisted Devices (" + allDevices.size() + "):");
                for (UsbWhitelistedDevice dev : allDevices) {
                    Log.d(TAG, "- VID: " + dev.getVidHex() + ", PID: " + dev.getPidHex() + ", Name: " + dev.getName() + ", Desc: " + dev.getDescription() + ", Persistence: " + dev.getPersistence());
                }

                // Example of removing a device
                 boolean removed = currentWhitelist.remove(device1);
                 Log.d(TAG, "Attempted to remove Device 1. Result: " + removed);
                 if(removed) {
                     verificationProp.set(currentWhitelist);
                     configManager.commit();
                     Log.d(TAG, "Committed removal of Device 1.");
                 }

            } else {
                Log.e(TAG, "Verification ERROR: Could not retrieve whitelist after commit.");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error during USB Whitelisting configuration: " + e.getMessage(), e);
        }
    }
}