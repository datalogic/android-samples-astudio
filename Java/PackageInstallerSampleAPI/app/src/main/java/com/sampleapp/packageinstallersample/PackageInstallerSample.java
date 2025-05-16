package com.sampleapp.packageinstallersample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PackageInstallerSample extends AppCompatActivity {
    private static final String TAG = "PackageInstallerSample";
    private EditText mPackageNameET;
    private Button mInstallButton;
    private Switch mSwitch;

    private TextView mLogTV;
    private TextView mTitleLogTV;
    private PackageInstallerHelper mPackageInstallerHelper;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPackageInstallerHelper = null;
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
        mPackageInstallerHelper = new PackageInstallerHelper(this);
    }
    private void initViews() {
        mPackageNameET = findViewById(R.id.et_package_name);

        mLogTV = findViewById(R.id.tv_log);
        mTitleLogTV = findViewById(R.id.tv_title_log);
        mSwitch = findViewById(R.id.sw_enable);
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSwitch.setText(R.string.enable);
                mInstallButton.setText(R.string.install_force_pgk);
            } else {
                mSwitch.setText(R.string.disable);
                mInstallButton.setText(R.string.install_not_force_pgk);
            }
        });

        mInstallButton = findViewById(R.id.btn_install_package);
        mInstallButton.setOnClickListener(v -> {
            InputData input = getInputData();
            if (mPackageInstallerHelper != null) {
                mPackageInstallerHelper.installPackage(input.packageName, input.enable);
                return;
            }
            Log.e(TAG, "ManagerHelper is null");
        });

    }

    private InputData getInputData() {
        InputData input = new InputData();
        input.packageName = mPackageNameET.getText().toString();
        input.enable = mSwitch.isChecked();

        return input;
    }
    public void showLog(String title, String log) {
        mTitleLogTV.setText(title);
        mLogTV.setText(log);
    }
    private final class InputData {
        String packageName;
        boolean enable;
    }
}