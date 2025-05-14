package com.datalogic.example.appforeground;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.datalogic.device.configuration.PropertyID;
import com.datalogic.device.PersistenceType;
import com.datalogic.device.configuration.ProfileManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MainActivity demonstrates how to use the Datalogic SDK to:
 * 1. Create a persistent configuration profile.
 * 2. Automatically apply that profile whenever the app enters the foreground.
 *
 * This is useful for enterprise workflows where configuration must be enforced
 * without user interaction.
 *
 * SDK Reference: https://datalogic.github.io/android/overview/
 */
public class MainActivity extends AppCompatActivity {

    private ProfileManager pm;

    // Profile and rule identifiers
    private static final String PROFILE_NAME = "testing_profile.json";
    private static final String RULE_NAME = "rule_test";
    private static final String PACKAGE_NAME = "com.datalogic.example.appforeground";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle safe window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ==========================================================
        // STEP 1: Initialize ProfileManager
        // ==========================================================
        // The ProfileManager allows you to programmatically manage configuration profiles on Datalogic devices.
        // It supports profile creation, editing, and binding to specific app events.
        //
        // Docs: https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/ProfileManager.html
        pm = new ProfileManager(this);

        // ==========================================================
        // STEP 2: Define Properties for the Profile
        // ==========================================================
        // The properties are stored in a HashMap using PropertyID keys.
        // You can use any supported configuration property.
        //
        // Docs for PropertyID: https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/device/configuration/PropertyID.html
        HashMap map = new HashMap();
        map.put(PropertyID.GREEN_SPOT_ENABLE, "false");  // Disable green spot indicator (BooleanProperty)
        map.put(PropertyID.LABEL_PREFIX, "testing");      // Add prefix to barcode label data (TextProperty)

        // ==========================================================
        // STEP 3: Create Profile with Persistence
        // ==========================================================
        // This creates a profile with the defined properties.
        // The persistence type ENTERPRISE_RESET_PERSISTENT means the profile survives device reboots and enterprise resets.
        pm.createProfile(
                PROFILE_NAME,
                map,
                "Example Profile",
                PersistenceType.ENTERPRISE_RESET_PERSISTENT
        );

        // ==========================================================
        // STEP 4: Bind the Profile to App Foreground Event
        // ==========================================================
        // This associates the created profile with this app’s foreground event.
        // The profile is applied automatically whenever the app enters the foreground.
        //
        // NOTE: The package name must match your app’s actual package name in AndroidManifest.
        pm.addProfileRule(
                new StringBuffer(RULE_NAME),
                PROFILE_NAME,
                PACKAGE_NAME,
                new ArrayList<>() // Optional intent filters
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ==========================================================
        // STEP 5: Cleanup on App Exit (Optional)
        // ==========================================================
        // Remove the profile rule and delete the profile on app exit.
        // This is helpful during testing to ensure clean state after each run.
        pm.removeProfileRule(RULE_NAME);
        pm.deleteProfile(PROFILE_NAME);
    }
}

