package com.datalogic.sampleapp.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.configuration.WebWedge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * WebWedgeFragment is an example of how to use the Datalogic SDK to configure the Scanner's Web Wedge.
 * <p>
 * The Web Wedge allows users to enable or disable the scanner's web wedge functionality.
 * This fragment demonstrates how to configure the Web Wedge using the Datalogic SDK.
 * </p>
 */
public class WebWedgeFragment extends Fragment {

    // ======================================================================
    // STEP 1: DECLARING VARIABLES
    // ======================================================================
    // The WebWedge object allows configuration of the scanner's web wedge.
    WebWedge mWebWedge;

    // The BarcodeManager manages the scanner's barcode-related functionalities.
    BarcodeManager mBarcodeManager;

    // UI components for user interaction.
    Button mStoreButton;
    Spinner mSpinnerWebWedge;

    // Map for boolean options (Enable/Disable).
    static Map<String, Boolean> kBooleanOptionsMap = new HashMap<>();

    static {
        kBooleanOptionsMap.put("Enable", true);
        kBooleanOptionsMap.put("Disable", false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ======================================================================
        // STEP 2: INITIALIZING THE BARCODE MANAGER
        // ======================================================================
        // The BarcodeManager allows interaction with the scanner's symbology.
        // It provides methods for enabling or disabling specific symbology.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/BarcodeManager.html
        //
        mBarcodeManager = new BarcodeManager();

        // ======================================================================
        // STEP 3: INITIALIZING THE WEB WEDGE
        // ======================================================================
        // The Web Wedge allows users to configure the scanner's web wedge.
        // It provides methods for enabling/disabling the web wedge functionality.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/WebWedge.html
        //
        mWebWedge = new WebWedge(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_wedge, container, false);

        mStoreButton = view.findViewById(R.id.apply_change_button);

        // Adapter for boolean values (Enable/Disable).
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(kBooleanOptionsMap.keySet())
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner for enabling/disabling the Web Wedge.
        mSpinnerWebWedge = view.findViewById(R.id.web_wedge_enable_value);
        mSpinnerWebWedge.setAdapter(adapter);

        mStoreButton.setOnClickListener(v -> {
            // ======================================================================
            // STEP 4: CHANGE WEB WEDGE ENABLE
            // ======================================================================
            // Retrieve the web wedge enable attribute from the spinner
            String webWedgeEnable = mSpinnerWebWedge.getSelectedItem().toString();
            // Set the enable/disable state for the Web Wedge
            mWebWedge.enable.set(kBooleanOptionsMap.get(webWedgeEnable));

            // ======================================================================
            // STEP 5: STORE WEB WEDGE CONFIGURATION
            // ======================================================================
            // Store the changes to persist the updated Web Wedge configuration
            mWebWedge.store(mBarcodeManager, true);
        });

        return view;
    }
}
