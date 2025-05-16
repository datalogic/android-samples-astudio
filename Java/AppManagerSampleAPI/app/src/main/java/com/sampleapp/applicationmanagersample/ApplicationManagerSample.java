package com.sampleapp.applicationmanagersample;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ApplicationManagerSample extends AppCompatActivity {
    private static final String TAG = "ApplicationManagerSample";
    private EditText mPackageNameET;
    private EditText mNotificationChannelET;
    private Switch mSwitch;
    private Button mGrantAllPermissionsButton;
    private Button mPermissionButton;
    private Button mBatteryOptimizationButton;
    private Button mHideApplicationButton;
    private Button mNotificationEnableButton;
    private Button mNotificationChannelButton;
    private TextView mLogTV;
    private TextView mTitleLogTV;
    private AppManagerHelper mAppAppManagerHelper;
    private PermissionAdapter mPermissionAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppAppManagerHelper = null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        initViews();
    }
    private void init() {
        mAppAppManagerHelper = new AppManagerHelper(this);
        mPermissionAdapter = new PermissionAdapter(this, AppManagerHelper.getAllPermissions());
    }
    private void initViews() {
        mPackageNameET = findViewById(R.id.et_package_name);
        mNotificationChannelET = findViewById(R.id.et_notification_channel);

        AutoCompleteTextView mPermissionACTV = findViewById(R.id.actv_permission);
        mPermissionACTV.setAdapter(mPermissionAdapter);
        mPermissionACTV.setOnClickListener(v -> mPermissionACTV.showDropDown());
        mPermissionACTV.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPermissionACTV.showDropDown();
            }
        });

        mLogTV = findViewById(R.id.tv_log);
        mTitleLogTV = findViewById(R.id.tv_title_log);

        Button mBtnDone = findViewById(R.id.btn_done);
        mBtnDone.setOnClickListener(v -> {
            StringBuilder log = new StringBuilder();
            for (String item : mPermissionAdapter.getSelectedItems()) {
                log.append(item).append("\n");
            }
            showLog("Selected permission:", log.toString());
        });

        mSwitch = findViewById(R.id.sw_enable);
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSwitch.setText(R.string.enable);
                mPermissionButton.setText(R.string.grant_permissions);
                mBatteryOptimizationButton.setText(R.string.add_battery_optimization_exemption);
                mHideApplicationButton.setText(R.string.hide_application);
                mNotificationEnableButton.setText(R.string.enable_notification_package);
                mNotificationChannelButton.setText(R.string.enable_notification_channel);
            } else {
                mSwitch.setText(R.string.disable);
                mPermissionButton.setText(R.string.revoke_permissions);
                mBatteryOptimizationButton.setText(R.string.remove_battery_optimization_exemption);
                mHideApplicationButton.setText(R.string.unhidden_application);
                mNotificationEnableButton.setText(R.string.disable_notification_package);
                mNotificationChannelButton.setText(R.string.disable_notification_channel);
            }
        });

        mGrantAllPermissionsButton = findViewById(R.id.btn_install_package);
        mGrantAllPermissionsButton.setOnClickListener(v -> {
            InputData input = getInputData();
            if (mAppAppManagerHelper != null) {
                mAppAppManagerHelper.grantAllPermissions(input.packageName);
                return;
            }
            Log.e(TAG, "ManagerHelper is null");
        });

        mPermissionButton = findViewById(R.id.btn_permission);
        mPermissionButton.setOnClickListener(v -> {
            InputData input = getInputData();
            mPermissionAdapter.clearSelectedItems();
            if (mAppAppManagerHelper != null) {
                boolean GRANT = mSwitch.isChecked();
                if (GRANT) {
                    mAppAppManagerHelper.grantPermissions(input.packageName, input.permissions);
                } else {
                    mAppAppManagerHelper.revokePermissions(input.packageName, input.permissions);
                }
                return;
            }
            Log.e(TAG, "ManagerHelper is null");
        });

        mBatteryOptimizationButton = findViewById(R.id.btn_battery_optimization);
        mBatteryOptimizationButton.setOnClickListener(v -> {
            if (mAppAppManagerHelper != null) {
                InputData input = getInputData();
                if (input.enable) {
                    mAppAppManagerHelper.addBatteryOptimizationExemption(input.packageName);
                } else {
                    mAppAppManagerHelper.removeBatteryOptimizationExemption(input.packageName);
                }
                return;
            }
            Log.e(TAG, "ManagerHelper is null");
        });

        mHideApplicationButton = findViewById(R.id.btn_application_visibility);
        mHideApplicationButton.setOnClickListener(v -> {
            InputData input = getInputData();
            if (mAppAppManagerHelper != null) {
                mAppAppManagerHelper.setApplicationHidden(input.packageName, input.enable);
                return;
            }
            Log.e(TAG, "ManagerHelper is null");
        });

        mNotificationChannelButton = findViewById(R.id.btn_notification_channel);
        mNotificationChannelButton.setOnClickListener(v -> {
            InputData input = getInputData();
            if (mAppAppManagerHelper != null) {
                mAppAppManagerHelper.enableNotificationChannel(input.packageName, input.notificationChannel, input.enable);
                return;
            }
            Log.e(TAG, "ManagerHelper is null");
        });

        mNotificationEnableButton = findViewById(R.id.btn_notification_enable);
        mNotificationEnableButton.setOnClickListener(v -> {
            InputData input = getInputData();
            if (mAppAppManagerHelper != null) {
                mAppAppManagerHelper.setNotificationsEnabledForPackage(input.packageName, input.enable);
                return;
            }
            Log.e(TAG, "ManagerHelper is null");
        });
    }

    private InputData getInputData() {
        InputData input = new InputData();
        input.packageName = mPackageNameET.getText().toString();
        input.notificationChannel = mNotificationChannelET.getText().toString();
        input.permissions = new ArrayList<>(mPermissionAdapter.getSelectedItems());
        input.enable = mSwitch.isChecked();
        return input;
    }
    public void showLog(String title, String log) {
        mTitleLogTV.setText(title);
        mLogTV.setText(log);
    }
    private final class InputData {
        String packageName;
        String notificationChannel;
        ArrayList<String> permissions;
        boolean enable;
    }
}