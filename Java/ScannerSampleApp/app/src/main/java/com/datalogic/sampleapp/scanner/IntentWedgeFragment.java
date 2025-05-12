package com.datalogic.sampleapp.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.configuration.IntentDeliveryMode;
import com.datalogic.decode.configuration.IntentWedge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IntentWedgeFragment is an example of how to use the Datalogic SDK to configure Scanner's intent wedge.
 * <p>
 * Intent Wedge let user can modify scanner's intent wedge
 * Links to the Datalogic SDK documentation for the relevant properties:
 * <ul>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/IntentDeliveryMode.html">IntentDeliveryMode</a></li>
 * </ul>
 * </p>
 */
public class IntentWedgeFragment extends Fragment {
    BarcodeManager mBarcodeManager;
    IntentWedge mIntentWedge;

    Spinner mSpinnerIntentEnable;
    Spinner mSpinnerDeliveryMode;
    EditText mEditTextAction;
    EditText mEditTextCategory;
    EditText mEditTextBarcodeData;
    EditText mEditTextBarcodeString;
    EditText mEditTextBarcodeType;

    Button mButtonStore;

    static List<IntentDeliveryMode> kIntentDeliveryModeOptionsList = Arrays.asList(IntentDeliveryMode.values());

    static Map<String, Boolean> kBooleanOptionsMap = new HashMap<>();

    static {
        kBooleanOptionsMap.put("Enable", true);
        kBooleanOptionsMap.put("Disable", false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ======================================================================
        // STEP 1: INITIALIZING THE BARCODE MANAGER
        // ======================================================================
        // The BarcodeManager allows interaction with the scanner's symbology.
        // It provides methods for enabling or disabling specific symbology.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/BarcodeManager.html
        //
        mBarcodeManager = new BarcodeManager();

        // ======================================================================
        // STEP 2: INITIALIZING THE INTENT WEDGE
        // ======================================================================
        // The Intent Wedge allows user config scanner's intent wedge.
        // It provides methods for scanner's intent wedge.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/IntentWedge.html
        //
        mIntentWedge = new IntentWedge(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intent_wedge, container, false);
        mButtonStore = view.findViewById(R.id.apply_change_button);

        // Adapter for boolean value
        ArrayAdapter<String> adapter = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , new ArrayList<>(kBooleanOptionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for intent delivery mode value
        ArrayAdapter<IntentDeliveryMode> adapterIntentDeliveryMode = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kIntentDeliveryModeOptionsList);
        adapterIntentDeliveryMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner for enable intent mode
        mSpinnerIntentEnable = view.findViewById(R.id.intent_enable_value);
        mSpinnerIntentEnable.setAdapter(adapter);

        // Spinner for delivery mode
        mSpinnerDeliveryMode = view.findViewById(R.id.delivery_mode_value);
        mSpinnerDeliveryMode.setAdapter(adapterIntentDeliveryMode);
        mSpinnerDeliveryMode.setSelection(2); // Default value for spinner

        // Edittext for action
        mEditTextAction = view.findViewById(R.id.action_value);

        // Edittext for category
        mEditTextCategory = view.findViewById(R.id.category_value);

        // Edittext for barcode data
        mEditTextBarcodeData = view.findViewById(R.id.barcode_data_value);

        // Edittext for barcode string
        mEditTextBarcodeString = view.findViewById(R.id.barcode_string_value);

        // Edittext for barcode type
        mEditTextBarcodeType = view.findViewById(R.id.barcode_type_value);

        // Handle business logic
        mButtonStore.setOnClickListener( v-> {
            // ======================================================================
            // STEP 3: ENABLE/DISABLE INTENT WEDGE
            // ======================================================================
            // Retrieve the intent wedge enable attribute from the spinner
            String intentValue = mSpinnerIntentEnable.getSelectedItem().toString();
            // Set the enable/disable state for the Intent Wedge
            mIntentWedge.enable.set(kBooleanOptionsMap.get(intentValue));

            // ======================================================================
            // STEP 4: CHANGE DELIVERY MODE
            // ======================================================================
            // Retrieve the delivery mode attribute from the spinner
            IntentDeliveryMode intentDeliveryMode = (IntentDeliveryMode) mSpinnerDeliveryMode.getSelectedItem();
            // Set the delivery mode for the Intent Wedge
            mIntentWedge.deliveryMode.set(intentDeliveryMode);

            // ======================================================================
            // STEP 5: SET ACTION STRING
            // ======================================================================
            // Retrieve the action string attribute from the input field
            String action = mEditTextAction.getText().toString();
            // Set the action string for the Intent Wedge
            mIntentWedge.action.set(action);

            // ======================================================================
            // STEP 6: SET CATEGORY STRING
            // ======================================================================
            // Retrieve the category string attribute from the input field
            String category = mEditTextCategory.getText().toString();
            // Set the category string for the Intent Wedge
            mIntentWedge.category.set(category);

            // ======================================================================
            // STEP 7: SET EXTRA BARCODE DATA
            // ======================================================================
            // Retrieve the extra barcode data attribute from the input field
            String barcodeData = mEditTextBarcodeData.getText().toString();
            // Set the extra barcode data for the Intent Wedge
            mIntentWedge.extraBarcodeData.set(barcodeData);

            // ======================================================================
            // STEP 8: SET EXTRA BARCODE STRING
            // ======================================================================
            // Retrieve the extra barcode string attribute from the input field
            String barcodeString = mEditTextBarcodeString.getText().toString();
            // Set the extra barcode string for the Intent Wedge
            mIntentWedge.extraBarcodeString.set(barcodeString);

            // ======================================================================
            // STEP 9: SET EXTRA BARCODE TYPE
            // ======================================================================
            // Retrieve the extra barcode type attribute from the input field
            String barcodeType = mEditTextBarcodeType.getText().toString();
            // Set the extra barcode type for the Intent Wedge
            mIntentWedge.extraBarcodeType.set(barcodeType);

            // ======================================================================
            // STEP 10: STORING THE CHANGES TO APPLY THE SCANNER'S INTENT WEDGE
            // ======================================================================
            // Store the changes to persist the updated Intent Wedge configuration
            // This saves and applies the changes to the scanner's intent wedge, making them persistent across reboots.
            mIntentWedge.store(mBarcodeManager, true);
        });
        return view;
    }
}
