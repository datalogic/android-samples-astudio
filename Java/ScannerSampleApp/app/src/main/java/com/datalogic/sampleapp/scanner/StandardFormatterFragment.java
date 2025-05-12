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
import com.datalogic.decode.configuration.ECIPolicy;
import com.datalogic.decode.configuration.Formatting;
import com.datalogic.decode.configuration.Gs1Conversion2d;
import com.datalogic.decode.configuration.Gs1LabelSetTransmitMode;
import com.datalogic.decode.configuration.GtinFormat;
import com.datalogic.decode.configuration.SendCodeID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StandardFormatterFragment is an example of how to use the Datalogic SDK to configure Scanner's format.
 * <p>
 * Standard Formatter let user can modify scanner's format
 * Links to the Datalogic SDK documentation for the relevant properties:
 * <ul>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/ECIPolicy.html">ECIPolicy</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/Gs1Conversion2d.html">Gs1Conversion2d</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/Gs1LabelSetTransmitMode.html">Gs1LabelSetTransmitMode</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/GtinFormat.html">GtinFormat</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/SendCodeID.html">SendCodeID</a></li>
 * </ul>
 * </p>
 */
public class StandardFormatterFragment extends Fragment {
    private static final String TAG = StandardFormatterFragment.class.getSimpleName();

    BarcodeManager mBarcodeManager;

    Formatting mFormatting;

    static List<ECIPolicy> kEciPolicyOptionsList = Arrays.asList(ECIPolicy.values());

    static List<Gs1Conversion2d> kGs1Conversion2dOptionsList = Arrays.asList(Gs1Conversion2d.values());

    static List<Gs1LabelSetTransmitMode> kGs1LabelSetTransmitModeOptionsList = Arrays.asList(Gs1LabelSetTransmitMode.values());

    static List<GtinFormat> kGtinFormatOptionsList = Arrays.asList(GtinFormat.values());

    static List<SendCodeID> kSendCodeIDOptionsList = Arrays.asList(SendCodeID.values());

    static Map<String, Boolean> kBooleanOptionsMap = new HashMap<>();

    static {
        kBooleanOptionsMap.put("Enable", true);
        kBooleanOptionsMap.put("Disable", false);
    }

    Button mStoreButton;
    Spinner mSpinnerECIPolicy;
    Spinner mSpinnerExternalFormatting;
    Spinner mSpinnerGs1Check;
    Spinner mSpinnerGs1Conversion2d;
    Spinner mSpinnerGs1LabelSetTransmitMode;
    Spinner mSpinnerGs1StringFormat;
    Spinner mSpinnerGtinFormat;
    Spinner mSpinnerHexFormat;
    Spinner mSpinnerRemoveNonPrintableChars;
    Spinner mSpinnerSendCodeId;
    EditText mEditTextGs1LabelSetPrefix;
    EditText mEditTextGsSubstitution;
    EditText mEditTextLabelPrefix;
    EditText mEditTextLabelSuffix;

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
        // STEP 2: INITIALIZING THE FORMATTING
        // ======================================================================
        // The Formatting allows user config format.
        // It provides methods for scanner's settings modification.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/Formatting.html
        //
        mFormatting = new Formatting(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standard_formatter, container, false);
        mStoreButton = view.findViewById(R.id.apply_change_button);

        mEditTextGs1LabelSetPrefix = view.findViewById(R.id.gs1_label_set_prefix_value);
        mEditTextGsSubstitution = view.findViewById(R.id.gs_substitution_value);
        mEditTextLabelPrefix = view.findViewById(R.id.label_prefix_value);
        mEditTextLabelSuffix = view.findViewById(R.id.label_suffix_value);

        // Adapter for boolean value
        ArrayAdapter<String> adapter = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , new ArrayList<>(kBooleanOptionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for ECI policy value
        ArrayAdapter<ECIPolicy> adapterECIPolicy = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kEciPolicyOptionsList);
        adapterECIPolicy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for Gs1 Conversion2d value
        ArrayAdapter<Gs1Conversion2d> adapterGs1Conversion2d = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kGs1Conversion2dOptionsList);
        adapterGs1Conversion2d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for Gs1 Label Set Transmit Mode value
        ArrayAdapter<Gs1LabelSetTransmitMode> adapterGs1LabelSetTransmitMode = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kGs1LabelSetTransmitModeOptionsList);
        adapterGs1LabelSetTransmitMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for Gtin Format value
        ArrayAdapter<GtinFormat> adapterGtinFormat = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kGtinFormatOptionsList);
        adapterGtinFormat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for Send CodeID value
        ArrayAdapter<SendCodeID> adapterSendCodeID = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , kSendCodeIDOptionsList);
        adapterSendCodeID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner for eci policy
        mSpinnerECIPolicy = view.findViewById(R.id.eci_policy_value);
        mSpinnerECIPolicy.setAdapter(adapterECIPolicy);

        // Spinner for external formatting
        mSpinnerExternalFormatting = view.findViewById(R.id.external_formatting_value);
        mSpinnerExternalFormatting.setAdapter(adapter);

        // Spinner for gs1 check
        mSpinnerGs1Check = view.findViewById(R.id.gs1_check_value);
        mSpinnerGs1Check.setAdapter(adapter);

        // Spinner for gs1 Conversion2d
        mSpinnerGs1Conversion2d = view.findViewById(R.id.gs1_conversion2d_value);
        mSpinnerGs1Conversion2d.setAdapter(adapterGs1Conversion2d);

        // Spinner for gs1 Label Set Transmit Mode
        mSpinnerGs1LabelSetTransmitMode = view.findViewById(R.id.gs1_label_set_transmit_mode_value);
        mSpinnerGs1LabelSetTransmitMode.setAdapter(adapterGs1LabelSetTransmitMode);

        // Spinner for gs1 String Format
        mSpinnerGs1StringFormat = view.findViewById(R.id.gs1_string_format_value);
        mSpinnerGs1StringFormat.setAdapter(adapter);

        // Spinner for gtin Format
        mSpinnerGtinFormat = view.findViewById(R.id.gtin_format_value);
        mSpinnerGtinFormat.setAdapter(adapterGtinFormat);

        // Spinner for hex Format
        mSpinnerHexFormat = view.findViewById(R.id.hex_format_value);
        mSpinnerHexFormat.setAdapter(adapter);

        // Spinner for remove non printable chars
        mSpinnerRemoveNonPrintableChars = view.findViewById(R.id.remove_non_printable_chars_value);
        mSpinnerRemoveNonPrintableChars.setAdapter(adapter);

        // Spinner for send code id
        mSpinnerSendCodeId = view.findViewById(R.id.send_codeid_value);
        mSpinnerSendCodeId.setAdapter(adapterSendCodeID);

        // Button for handling business logic
        mStoreButton.setOnClickListener(v->{
            // ======================================================================
            // STEP 3: CHANGE ECI POLICY
            // ======================================================================
            // Retrieve the eci policy attribute from formatting
            ECIPolicy eciPolicy = (ECIPolicy) mSpinnerECIPolicy.getSelectedItem();
            mFormatting.eciPolicy.set(eciPolicy);

            // ======================================================================
            // STEP 4: ENABLE/DISABLE EXTERNAL FORMATTING
            // ======================================================================
            // Retrieve the external formatting attribute from formatting
            String externalFormatting = mSpinnerExternalFormatting.getSelectedItem().toString();
            mFormatting.externalFormatting.set(kBooleanOptionsMap.get(externalFormatting));

            // ======================================================================
            // STEP 5: ENABLE/DISABLE GS1 CHECK
            // ======================================================================
            // Retrieve the gs1 check attribute from formatting
            String gs1Check =  mSpinnerGs1Check.getSelectedItem().toString();
            mFormatting.gs1Check.set(kBooleanOptionsMap.get(gs1Check));

            // ======================================================================
            // STEP 6: CHANGE GS1 CONVERSION 2D
            // ======================================================================
            // Retrieve the gs1 conversion 2d attribute from formatting
            Gs1Conversion2d gs1Conversion2d = (Gs1Conversion2d) mSpinnerGs1Conversion2d.getSelectedItem();
            mFormatting.gs1Conversion2d.set(gs1Conversion2d);

            // ======================================================================
            // STEP 7: CHANGE GS1 LABEL PREFIX
            // ======================================================================
            // Retrieve the gs1 label prefix attribute from formatting
            String gs1LabelSetPrefix = mEditTextGs1LabelSetPrefix.getText().toString();
            mFormatting.gs1LabelSetPrefix.set(gs1LabelSetPrefix);

            // ======================================================================
            // STEP 8: CHANGE GS1 LABEL TRANSMIT
            // ======================================================================
            // Retrieve the gs1 label transmit attribute from formatting
            Gs1LabelSetTransmitMode gs1LabelSetTransmitMode = (Gs1LabelSetTransmitMode) mSpinnerGs1LabelSetTransmitMode.getSelectedItem();
            mFormatting.gs1LabelSetTransmitMode.set(gs1LabelSetTransmitMode);

            // ======================================================================
            // STEP 9: CHANGE GS1 STRING FORMAT
            // ======================================================================
            // Retrieve the gs1 string format attribute from formatting
            String gs1StringFormat = mSpinnerGs1StringFormat.getSelectedItem().toString();
            mFormatting.gs1StringFormat.set(kBooleanOptionsMap.get(gs1StringFormat));

            // ======================================================================
            // STEP 10: CHANGE GTIN FORMAT
            // ======================================================================
            // Retrieve the gtin format attribute from formatting
            GtinFormat gtinFormat = (GtinFormat) mSpinnerGtinFormat.getSelectedItem();
            mFormatting.gtinFormat.set(gtinFormat);

            // ======================================================================
            // STEP 11: CHANGE HEX FORMAT
            // ======================================================================
            // Retrieve the hex format attribute from formatting
            String hexFormat = mSpinnerHexFormat.getSelectedItem().toString();
            mFormatting.hexFormat.set(kBooleanOptionsMap.get(hexFormat));

            // ======================================================================
            // STEP 12: ENABLE/DISABLE REMOVE NON PRINTABLE CHARS
            // ======================================================================
            // Retrieve the remove non printable chars attribute from formatting
            String removeNonPrintableChars = mSpinnerRemoveNonPrintableChars.getSelectedItem().toString();
            mFormatting.removeNonPrintableChars.set(kBooleanOptionsMap.get(removeNonPrintableChars));

            // ======================================================================
            // STEP 13: CHANGE SEND CODE ID
            // ======================================================================
            // Retrieve the send code id attribute from formatting
            SendCodeID sendCodeID = (SendCodeID) mSpinnerSendCodeId.getSelectedItem();
            mFormatting.sendCodeId.set(sendCodeID);

            // ======================================================================
            // STEP 14: CHANGE GS SUBSTITUTION
            // ======================================================================
            // Retrieve the gs substitution attribute from formatting
            String gsSubstitution = mEditTextGsSubstitution.getText().toString();
            mFormatting.gsSubstitution.set(gsSubstitution);

            // ======================================================================
            // STEP 15: CHANGE LABEL PREFIX
            // ======================================================================
            // Retrieve the label prefix attribute from formatting
            String labelPrefix = mEditTextLabelPrefix.getText().toString();
            mFormatting.labelPrefix.set(labelPrefix);

            // ======================================================================
            // STEP 15: CHANGE LABEL SUFFIX
            // ======================================================================
            // Retrieve the label suffix attribute from formatting
            String labelSuffix = mEditTextLabelSuffix.getText().toString();
            mFormatting.labelSuffix.set(labelSuffix);

            // ======================================================================
            // STEP 16: STORING THE CHANGES TO APPLY THE SCANNER'S FORMATTING
            // ======================================================================
            // Store the changes to persist the updated settings to the device's storage.
            // This saves and applies the changes to the scanner's formatting, making them persistent across reboots.
            mFormatting.store(mBarcodeManager, true);
        });


        return view;
    }
}
