package com.datalogic.example.configurationmanagersampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

// Necessary imports for Datalogic SDK classes
import com.datalogic.device.configuration.BlobProperty;
import com.datalogic.device.configuration.ConfigurationManager;
import com.datalogic.device.configuration.DeviceNameSuffix; // Enum for DEVICE_NAME_SUFFIX
import com.datalogic.device.configuration.PropertyID;     // Contains the property identifiers
import com.datalogic.device.configuration.BooleanProperty;
import com.datalogic.device.configuration.NumericProperty;
import com.datalogic.device.configuration.StringSetBlob; // Specific Blob type for VIRTUAL_KEYBOARDS_CUSTOM_ENABLED
import com.datalogic.device.configuration.TextProperty;
import com.datalogic.device.configuration.EnumProperty;

/**
 * MainActivity is an example demonstrating how to use the Datalogic SDK's ConfigurationManager interface
 * to programmatically view and modify various device settings. This sample provides a practical guide
 * for developers looking to integrate device configuration capabilities into their applications.
 * <p>
 * This sample showcases the common workflow for managing diverse device properties:
 * <ul>
 * <li>Obtaining an instance of {@link com.datalogic.device.configuration.ConfigurationManager}, which is the entry point for all configuration tasks.</li>
 * <li>Retrieving specific properties by their unique identifiers using {@link com.datalogic.device.configuration.PropertyID}.</li>
 * <li>Interacting with various types of properties, including:
 * <ul>
 * <li>{@link com.datalogic.device.configuration.BooleanProperty} (e.g., for managing true/false settings like {@code PropertyID.LONG_PRESS_HOME_BUTTON_ENABLED}).</li>
 * <li>{@link com.datalogic.device.configuration.NumericProperty} (e.g., for handling numerical settings like {@code PropertyID.SCREEN_BRIGHTNESS}).</li>
 * <li>{@link com.datalogic.device.configuration.TextProperty} (e.g., for string-based configurations like {@code PropertyID.DEVICE_NAME_BASE}).</li>
 * <li>{@link com.datalogic.device.configuration.EnumProperty}, used with enums like {@link com.datalogic.device.configuration.DeviceNameSuffix} (e.g., for settings with predefined options like {@code PropertyID.DEVICE_NAME_SUFFIX}).</li>
 * <li>{@link com.datalogic.device.configuration.BlobProperty}, specifically demonstrating the use of {@link com.datalogic.device.configuration.StringSetBlob} (e.g., for managing a list of custom keyboard IME IDs via {@code PropertyID.VIRTUAL_KEYBOARDS_CUSTOM_ENABLED}).</li>
 * </ul>
 * </li>
 * <li>Verifying if a specific property {@link com.datalogic.device.configuration.Property#isSupported() isSupported} on the current device.</li>
 * <li>Reading the current value of a property using the {@link com.datalogic.device.configuration.Property#get() get()} method.</li>
 * <li>Modifying the value of a property using the {@link com.datalogic.device.configuration.Property#set(Object) set()} method, which stages the change.</li>
 * <li>Persisting all staged changes to the device's configuration using the {@link com.datalogic.device.configuration.ConfigurationManager#commit() commit()} method.</li>
 * </ul>
 * The {@code ConfigurationManager} gives the developer the ability to browse and set the configuration properties of the device. The device, as a managed device, exposes properties organized in a tree.
 * Each property has a unique identifier used to uniquely identify the property. ConfigurationManager is the sdk access point to the whole of properties that describes and allows the configuration of the device.
 * </p>
 * <p>
 * For more detailed information on the Datalogic SDK classes, property identifiers, and configuration concepts demonstrated in this sample,
 * please refer to the official Datalogic Android SDK documentation:
 * <ul>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/ConfigurationManager.html">ConfigurationManager</a> - The main class for managing device configurations.</li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html">PropertyID</a> - Defines unique identifiers for all configurable properties.</li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/BooleanProperty.html">BooleanProperty</a> - For true/false configuration values.</li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/NumericProperty.html">NumericProperty</a> - For numerical configuration values.</li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/TextProperty.html">TextProperty</a> - For text-based (string) configuration values.</li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/EnumProperty.html">EnumProperty</a> - For configuration values chosen from a predefined set (enumeration).</li>
 * <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/BlobProperty.html">BlobProperty</a> - For complex or binary data configurations.</li>
 * </ul>
 * </p>
 */
public class MainActivity extends Activity {
    private static final String TAG = "ConfigManagerSampleApi";
    private ConfigurationManager configManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial setup
        boolean isSupported; // Variable to store support status
        int setResult;       // Variable to store the result of .set() operations (0 for success)

        // STEP 1: INITIALIZING THE CONFIGURATION MANAGER
        // The Configuration Manager allows interaction with the device's configuration properties.
        configManager = new ConfigurationManager(getApplicationContext());

        // STEP 2: GET PROPERTY, CHECK IF SUPPORTED, GET PROPERTY VALUE AND SET A NEW ONE

        // --- Example 1. Boolean Property ---
        // This section demonstrates handling a simple boolean (true/false) property.
        // Use LONG_PRESS_HOME_BUTTON_ENABLED as an example. See: https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html#LONG_PRESS_HOME_BUTTON_ENABLED

        // Get the specific BooleanProperty using its ID
        BooleanProperty longPressHomeEnabledProp = (BooleanProperty) configManager.getPropertyById(PropertyID.LONG_PRESS_HOME_BUTTON_ENABLED);
        // Check if the property is supported on this device
        isSupported = longPressHomeEnabledProp.isSupported();
        Log.d(TAG, "LONG_PRESS_HOME_BUTTON_ENABLED is supported: " + isSupported);

        if (isSupported) {
            // Get the current boolean value
            boolean currentValue = longPressHomeEnabledProp.get();
            Log.d(TAG, "Current LONG_PRESS_HOME_BUTTON_ENABLED value: " + currentValue);

            // Set a new value (toggling the current value)
            // It's important to note that:
            // The .set() method stages the change; it's not applied until commit()
            setResult = longPressHomeEnabledProp.set(!currentValue);
            Log.d(TAG, "Set LONG_PRESS_HOME_BUTTON_ENABLED to " + !currentValue + ". Result: " + (setResult == 0 ? "SUCCESS" : "FAILURE code " + setResult));
        }

        // --- 2. Numeric Property ---
        // This demonstrates handling a property that takes a numerical value (integer in this case).
        // Use SCREEN_BRIGHTNESS as an example. See: https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html#SCREEN_BRIGHTNESS
        NumericProperty screenBrightnessProp = (NumericProperty) configManager.getPropertyById(PropertyID.SCREEN_BRIGHTNESS);
        // Check if the property is supported on this device
        isSupported = screenBrightnessProp.isSupported();
        Log.d(TAG, "SCREEN_BRIGHTNESS is supported: " + isSupported);

        if (isSupported) {
            // Get the current brightness value. NumericProperty.get() returns an Integer.
            Integer currentValue = screenBrightnessProp.get();
            Log.d(TAG, "Current SCREEN_BRIGHTNESS value: " + currentValue);

            // Set a new brightness value (e.g., 255 for maximum)
            Integer newValue = 255;
            setResult = screenBrightnessProp.set(newValue);
            Log.d(TAG, "Set SCREEN_BRIGHTNESS to " + newValue + ". Result: " + (setResult == 0 ? "SUCCESS" : "FAILURE code " + setResult));
        }


        // --- 3. Text Property ---
        // This demonstrates handling a property that takes a string of text.
        // Use DEVICE_NAME_BASE as an example. See: https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html#DEVICE_NAME_BASE
        TextProperty deviceNameBaseProp = (TextProperty) configManager.getPropertyById(PropertyID.DEVICE_NAME_BASE);
        // Check if the property is supported on this device
        isSupported = deviceNameBaseProp.isSupported();
        Log.d(TAG, "DEVICE_NAME_BASE isSupported: " + isSupported);

        if (isSupported) {
            // Get the current device name base
            String currentValue = deviceNameBaseProp.get();
            Log.d(TAG, "Current DEVICE_NAME_BASE value: \"" + currentValue + "\"");

            // Set a new device name base
            String newValue = "DatalogicDevice";
            setResult = deviceNameBaseProp.set(newValue);
            Log.d(TAG, "Set DEVICE_NAME_BASE to \"" + newValue + "\". Result: " + (setResult == 0 ? "SUCCESS" : "FAILURE code " + setResult));
        }

        // --- 4. Enum Property ---
        // This demonstrates handling a property whose value must be one of a predefined set of options (an enumeration).
        // Use DEVICE_NAME_SUFFIX as an example. See: https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html#DEVICE_NAME_SUFFIX
        // The Datalogic SDK provides the DeviceNameSuffix enum for this specific property.
        /* Enum values for DeviceNameSuffix:
           DeviceNameSuffix.MAC_ADDRESS
           DeviceNameSuffix.NIC_SPECIFIC_MAC_ADDRESS
           DeviceNameSuffix.NONE
           DeviceNameSuffix.SERIAL_NUMBER
        */
        EnumProperty deviceNameSuffixProp = (EnumProperty) configManager.getPropertyById(PropertyID.DEVICE_NAME_SUFFIX);
        // Check if the property is supported on this device
        isSupported = deviceNameSuffixProp.isSupported();
        Log.d(TAG, "DEVICE_NAME_SUFFIX isSupported: " + isSupported);

        if (isSupported) {
            // Get the current enum value. The .get() method on EnumProperty typically returns an object
            // that needs to be cast to the specific Enum type (DeviceNameSuffix in this case).
            DeviceNameSuffix deviceNameSuffix = (DeviceNameSuffix) deviceNameSuffixProp.get();
            Log.d(TAG, "Current DEVICE_NAME_SUFFIX value: " + deviceNameSuffix.toString());

            // Set a new enum value using one of the defined constants from DeviceNameSuffix
            setResult = deviceNameSuffixProp.set(DeviceNameSuffix.SERIAL_NUMBER);
            Log.d(TAG, "Set DEVICE_NAME_SUFFIX to " + DeviceNameSuffix.SERIAL_NUMBER + ". Result: " + (setResult == 0 ? "SUCCESS" : "FAILURE code " + setResult));
        }

        // --- 5. Blob/Bundle Property ---
        // This section handles a more complex property type, a BlobProperty.
        // Use VIRTUAL_KEYBOARDS_CUSTOM_ENABLED (StringSetBlob) as an example. See: https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html#VIRTUAL_KEYBOARDS_CUSTOM_ENABLED
        // In this specific case,
        // it's a StringSetBlob, meaning it stores a set of strings (keyboard IDs).
        BlobProperty virtualKeyboardsProp = (BlobProperty) configManager.getPropertyById(PropertyID.VIRTUAL_KEYBOARDS_CUSTOM_ENABLED);
        // Check if the property is supported on this device
        isSupported = virtualKeyboardsProp.isSupported();
        Log.d(TAG, "VIRTUAL_KEYBOARDS_CUSTOM_ENABLED isSupported: " + isSupported);

        if (isSupported) {
            // Get the current StringSetBlob object. BlobProperty.get() returns the specific blob type.
            StringSetBlob currentVirtualKeyboard = (StringSetBlob) virtualKeyboardsProp.get();
            // Log current state. currentVirtualKeyboard.get() likely returns the underlying Set<String>.
            Log.d(TAG, "Current total custom virtual keyboards: " + currentVirtualKeyboard.size());

            // Method 1: Replace existing set:
            // To set a new value for a StringSetBlob, first we create a new StringSetBlob instance,
            // populate it, and then use .set() on the original BlobProperty.
            // This replaces the entire existing set with the new one.
            StringSetBlob newValueToSet = new StringSetBlob(); // Create a new StringSetBlob

            newValueToSet.add("com.google.android.tts/com.google.android.apps.speech.tts.googletts.settings.asr.voiceime.VoiceInputMethodService"); // Add the desired keyboard ID(s) to this new set
            setResult = virtualKeyboardsProp.set(newValueToSet); // Set the BlobProperty to this new StringSetBlob
            Log.d(TAG, "Set VIRTUAL_KEYBOARDS_CUSTOM_ENABLED to " + newValueToSet.get() + ". Result: " + (setResult == 0 ? "SUCCESS" : "FAILURE code " + setResult));

            // Method 2: Modify existing set:
            // If we still want to add to the existing set, we will typically do:
            // currentVirtualKeyboard.add("some.new.keyboard/.ID");
            // setResult = virtualKeyboardsProp.set(currentVirtualKeyboard);
        }

        // STEP 3: COMMITTING THE CHANGES TO APPLY THE CONFIGURATION
        // This saves and applies all the staged changes (from .set() calls) to the device configuration,
        // making them persistent across reboots.
        configManager.commit();
        Log.d(TAG, "Configuration changes committed.");
    }
}