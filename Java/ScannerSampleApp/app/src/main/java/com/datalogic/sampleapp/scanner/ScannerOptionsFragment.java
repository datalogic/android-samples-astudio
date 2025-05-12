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

import com.datalogic.decode.configuration.BeamMode;
import com.datalogic.decode.configuration.IlluminationTime;
import com.datalogic.decode.configuration.IlluminationType;
import com.datalogic.decode.configuration.ImageCaptureProfile;
import com.datalogic.decode.configuration.ScanMode;
import com.datalogic.decode.*;
import com.datalogic.decode.configuration.ScannerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ScannerOptionsFragment is an example of how to use the Datalogic SDK to configure Scanner's settings.
 * <p>
 * Scan Options let user can modify scanner's behavior
 * Links to the Datalogic SDK documentation for the relevant properties:
 * <ul>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/ImageCaptureProfile.html">ImageCaptureProfile</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/IlluminationType.html">IlluminationType</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/ScanMode.html">ScanMode</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/BeamMode.html">BeamMode</a></li>
 *   <li><a href="https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/IlluminationTime.html">IlluminationTime</a></li>
 * </ul>
 * </p>
 */
public class ScannerOptionsFragment extends Fragment {
    private static final String TAG = ScannerOptionsFragment.class.getSimpleName();

    BarcodeManager mBarcodeManager;

    com.datalogic.decode.configuration.ScannerOptions mScannerOptions;

    Button mStoreButton;

    static Map<String, Boolean> kBooleanOptionsMap = new HashMap<>();
    static List<BeamMode> kBeamModeOptionsList = Arrays.asList(BeamMode.values());
    static Map<Double, Integer> kTargetTimeoutOptionsMap = new HashMap<>();
    static {
        // Initialize boolean map
        kBooleanOptionsMap.put("Enable", true);
        kBooleanOptionsMap.put("Disable", false);

        // Initialize target timeout map
        kTargetTimeoutOptionsMap.put(0.25, 0);
        kTargetTimeoutOptionsMap.put(0.5, 1);
        kTargetTimeoutOptionsMap.put(1.0, 2);
        kTargetTimeoutOptionsMap.put(1.5, 3);
        kTargetTimeoutOptionsMap.put(2.0, 4);
    }

    static List<ImageCaptureProfile> kImageCaptureProfileOptionsList = Arrays.asList(ImageCaptureProfile.values());

    static List<IlluminationType> kIlluminationTypeOptionsList = Arrays.asList(IlluminationType.values());

    static List<ScanMode> kScanModeOptionsList = Arrays.asList(ScanMode.values());

    static List<IlluminationTime> kIlluminationTimeOptionsList = Arrays.asList(IlluminationTime.values());


    Spinner mSpinnerDisplayMode;

    Spinner mSpinnerIlluminationMode;

    Spinner mSpinnerAimMode;
    Spinner mSpinnerPickListMode;
    Spinner mSpinnerBeamMode;

    Spinner mSpinnerTargetMode;

    Spinner mSpinnerTargetTimeout;

    EditText mEditTextTargetReleaseTimeout;

    EditText mEditTextDecodeTimeout;

    Spinner mSpinnerImageCaptureProfile;

    EditText mEditTextCustomImageCaptureProfile;

    Spinner mSpinnerIlluminationType;

    Spinner mSpinnerScanMode;

    EditText mEditTextDoubleReadTimeout;

    Spinner mSpinnerIlluminationTime;

    Spinner mSpinnerEnhanceDOFEnable;

    EditText mEditTextImageDecodeTimeout;

    Spinner mSpinnerScannerMode;


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
        // STEP 2: INITIALIZING THE SCANNER OPTIONS
        // ======================================================================
        // The ScannerOptions allows interaction with the scanner's functionality.
        // It provides methods for scanner's settings modification.
        //
        // Refer to the official Datalogic SDK documentation here:
        // https://datalogic.github.io/android-sdk-docs/reference/com/datalogic/decode/configuration/ScannerOptions.html
        //
        mScannerOptions = new ScannerOptions(mBarcodeManager);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner_options, container, false);

        mStoreButton = view.findViewById(R.id.apply_change_button);

        // Adapter for boolean value
        ArrayAdapter<String> adapter = new ArrayAdapter<>(                                                                                                                                                                                getContext()
                , android.R.layout.simple_spinner_item
                , new ArrayList<>(kBooleanOptionsMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for beam value
        ArrayAdapter<BeamMode> adapterBeamMode = new ArrayAdapter<>(getContext()
                , android.R.layout.simple_spinner_item, kBeamModeOptionsList);
        adapterBeamMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for target timeout
        List<Double> sortedKeys = new ArrayList<>(kTargetTimeoutOptionsMap.keySet());
        Collections.sort(sortedKeys);
        ArrayAdapter<Double> adapterTargetTimeoutMode = new ArrayAdapter<>(getContext()
                , android.R.layout.simple_spinner_item, sortedKeys);
        adapterTargetTimeoutMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for image capture profile value
        ArrayAdapter<ImageCaptureProfile> adapterImageCaptureProfile = new ArrayAdapter<>(getContext()
                , android.R.layout.simple_spinner_item, kImageCaptureProfileOptionsList);
        adapterImageCaptureProfile.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for illumination type value
        ArrayAdapter<IlluminationType> adapterIlluminationType = new ArrayAdapter<>(getContext()
                , android.R.layout.simple_spinner_item, kIlluminationTypeOptionsList);
        adapterIlluminationType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for scan mode value
        ArrayAdapter<ScanMode> adapterScanMode = new ArrayAdapter<>(getContext()
                , android.R.layout.simple_spinner_item, kScanModeOptionsList);
        adapterScanMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter for illumination time
        ArrayAdapter<IlluminationTime> adapterIlluminationTime = new ArrayAdapter<>(getContext()
                , android.R.layout.simple_spinner_item, kIlluminationTimeOptionsList);
        adapterIlluminationTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner for display mode
        mSpinnerDisplayMode = view.findViewById(R.id.display_mode_value);
        mSpinnerDisplayMode.setAdapter(adapter);

        // Spinner for illumination mode
        mSpinnerIlluminationMode = view.findViewById(R.id.illumination_mode_value);
        mSpinnerIlluminationMode.setAdapter(adapter);

        // Spinner for aim mode
        mSpinnerAimMode = view.findViewById(R.id.aim_mode_value);
        mSpinnerAimMode.setAdapter(adapter);

        // Spinner for pick list
        mSpinnerPickListMode = view.findViewById(R.id.pick_list_value);
        mSpinnerPickListMode.setAdapter(adapter);

        // Spinner for target mode
        mSpinnerTargetMode = view.findViewById(R.id.target_mode_value);
        mSpinnerTargetMode.setAdapter(adapter);

        // Spinner for beam mode
        mSpinnerBeamMode = view.findViewById(R.id.beam_mode_value);
        mSpinnerBeamMode.setAdapter(adapterBeamMode);

        // Spinner for target timeout
        mSpinnerTargetTimeout = view.findViewById(R.id.target_timeout_value);
        mSpinnerTargetTimeout.setAdapter(adapterTargetTimeoutMode);

        // Edittext for target release scan
        mEditTextTargetReleaseTimeout = view.findViewById(R.id.target_release_timeout_value);

        // Edittext for decode timeout
        mEditTextDecodeTimeout = view.findViewById(R.id.decode_timeout_value);

        // Spinner for image capture profile
        mSpinnerImageCaptureProfile = view.findViewById(R.id.target_image_capture_profile_value);
        mSpinnerImageCaptureProfile.setAdapter(adapterImageCaptureProfile);

        // Edittext for custom image capture profile
        mEditTextCustomImageCaptureProfile = view.findViewById(R.id.custom_image_capture_profile_value);

        // Spinner for Illumination Type
        mSpinnerIlluminationType = view.findViewById(R.id.illumination_type_value);
        mSpinnerIlluminationType.setAdapter(adapterIlluminationType);

        // Spinner for Scan Mode
        mSpinnerScanMode = view.findViewById(R.id.scan_mode_value);
        mSpinnerScanMode.setAdapter(adapterScanMode);

        // Edittext for double read timeout
        mEditTextDoubleReadTimeout = view.findViewById(R.id.double_read_timeout_value);

        // Spinner for Illumination Time
        mSpinnerIlluminationTime = view.findViewById(R.id.illumination_time_value);
        mSpinnerIlluminationTime.setAdapter(adapterIlluminationTime);

        // Spinner for Illumination Time
        mSpinnerEnhanceDOFEnable = view.findViewById(R.id.enhance_dof_enable_value);
        mSpinnerEnhanceDOFEnable.setAdapter(adapter);

        // Edittext for image decode timeout
        mEditTextImageDecodeTimeout = view.findViewById(R.id.image_decode_value);

        // Spinner for scanner
        mSpinnerScannerMode = view.findViewById(R.id.enable_scanner_value);
        mSpinnerScannerMode.setAdapter(adapter);

        // Logic for Change scanner option
        mStoreButton.setOnClickListener(v->{
            // ======================================================================
            // STEP 3: ENABLE/DISABLE THE DISPLAY MODE
            // ======================================================================
            // Retrieve the display mode attribute from scanner options
            String displayMode = mSpinnerDisplayMode.getSelectedItem().toString();
            mScannerOptions.displayModeEnable.set(kBooleanOptionsMap.get(displayMode));

            // ======================================================================
            // STEP 4: ENABLE/DISABLE THE ILLUMINATION MODE
            // ======================================================================
            // Retrieve the illumination mode attribute from scanner options
            String illuminationMode = mSpinnerIlluminationMode.getSelectedItem().toString();
            mScannerOptions.illuminationEnable.set(kBooleanOptionsMap.get(illuminationMode));

            // ======================================================================
            // STEP 5: ENABLE/DISABLE THE AIM MODE
            // ======================================================================
            // Retrieve the aim mode attribute from scanner options
            String aimMode = mSpinnerAimMode.getSelectedItem().toString();
            mScannerOptions.aimEnable.set(kBooleanOptionsMap.get(aimMode));

            // ======================================================================
            // STEP 6: ENABLE/DISABLE THE PICK LIST
            // ======================================================================
            // Retrieve the pick list attribute from scanner options
            String picklistMode = mSpinnerPickListMode.getSelectedItem().toString();
            mScannerOptions.picklistEnable.set(kBooleanOptionsMap.get(picklistMode));

            // ======================================================================
            // STEP 7: ENABLE/DISABLE THE TARGET MODE
            // ======================================================================
            // Retrieve the target mode attribute from scanner options
            String targetMode = mSpinnerTargetMode.getSelectedItem().toString();
            mScannerOptions.targetModeEnable.set(kBooleanOptionsMap.get(targetMode));

            // ======================================================================
            // STEP 8: CHANGE THE BEAM MODE
            // ======================================================================
            // Retrieve the beam mode attribute from scanner options
            BeamMode beamMode = (BeamMode) mSpinnerBeamMode.getSelectedItem();
            mScannerOptions.targetMode.set(beamMode);

            if (beamMode == BeamMode.TARGET_TIMEOUT) {
                // Change target timeout
                Double targetTimeout = (Double) mSpinnerTargetTimeout.getSelectedItem();
                mScannerOptions.targetTimeout.set(kTargetTimeoutOptionsMap.get(targetTimeout));
            } else {
                // Change target release scan
                int targetReleaseScan =  Integer.parseInt(mEditTextTargetReleaseTimeout.getText().toString());
                mScannerOptions.targetReleaseTimeout.set(targetReleaseScan);
            }

            // ======================================================================
            // STEP 9: CHANGE THE DECODE TIMEOUT
            // ======================================================================
            // Retrieve the decode timeout attribute from scanner options
            int decodeTimeout = Integer.parseInt(mEditTextDecodeTimeout.getText().toString());
            mScannerOptions.decodeTimeout.set(decodeTimeout);

            // ======================================================================
            // STEP 10: CHANGE THE IMAGE CAPTURE PROFILE
            // ======================================================================
            // Retrieve the image capture profile attribute from scanner options
            ImageCaptureProfile imageCaptureProfile = (ImageCaptureProfile) mSpinnerImageCaptureProfile.getSelectedItem();
            mScannerOptions.imageCaptureProfile.set(imageCaptureProfile);

            // ======================================================================
            // STEP 11: CHANGE THE CUSTOM IMAGE CAPTURE PROFILE
            // ======================================================================
            // Retrieve the custom image capture profile attribute from scanner options
            int customImageCaptureProfile = Integer.parseInt(mEditTextCustomImageCaptureProfile.getText().toString());
            mScannerOptions.customImageCaptureProfile.set(customImageCaptureProfile);

            // ======================================================================
            // STEP 12: CHANGE THE ILLUMINATION TYPE
            // ======================================================================
            // Retrieve the illumination type attribute from scanner options
            IlluminationType illuminationType = (IlluminationType) mSpinnerIlluminationType.getSelectedItem();
            mScannerOptions.illuminationType.set(illuminationType);

            // ======================================================================
            // STEP 13: CHANGE THE SCAN MODE
            // ======================================================================
            // Retrieve the scan mode attribute from scanner options
            ScanMode scanMode = (ScanMode) mSpinnerScanMode.getSelectedItem();
            mScannerOptions.scanMode.set(scanMode);

            // ======================================================================
            // STEP 14: CHANGE DOUBLE READ TIMEOUT
            // ======================================================================
            // Retrieve the double read timeout attribute from scanner options
            int doubleReadTimeout = Integer.parseInt(mEditTextDoubleReadTimeout.getText().toString());
            mScannerOptions.doubleReadTimeout.set(doubleReadTimeout);

            // ======================================================================
            // STEP 15: CHANGE ILLUMINATION TIME
            // ======================================================================
            // Retrieve the illumination time attribute from scanner options
            IlluminationTime illuminationTime = (IlluminationTime) mSpinnerIlluminationTime.getSelectedItem();
            mScannerOptions.illuminationTime.set(illuminationTime);

            // ======================================================================
            // STEP 16: CHANGE DEPTH OF FIELD
            // ======================================================================
            // Retrieve the dof attribute from scanner options
            String enhanceDof = mSpinnerEnhanceDOFEnable.getSelectedItem().toString();
            mScannerOptions.enhanceDOFEnable.set(kBooleanOptionsMap.get(enhanceDof));

            // ======================================================================
            // STEP 17: CHANGE DECODE TIMEOUT
            // ======================================================================
            // Retrieve the image decode timeout attribute from scanner options
            int imageDecodeTimeout = Integer.parseInt(mEditTextImageDecodeTimeout.getText().toString());
            mScannerOptions.imageDecodeTimeout.set(imageDecodeTimeout);

            // ======================================================================
            // STEP 18: ENABLE/DISABLE SCANNER MODE
            // ======================================================================
            // Retrieve the scanner mode attribute from scanner options
            String scannerMode = mSpinnerScannerMode.getSelectedItem().toString();
            mScannerOptions.enableScanner.set(kBooleanOptionsMap.get(scannerMode));

            // ======================================================================
            // STEP 19: STORING THE CHANGES TO APPLY THE SCANNER'S SETTINGS
            // ======================================================================
            // Store the changes to persist the updated settings to the device's storage.
            // This saves and applies the changes to the scanner's settings, making them persistent across reboots.
            mScannerOptions.store(mBarcodeManager, true);
        });

        return view;
    }
}
