// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodesampleapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.decode.StartListener;
import com.datalogic.decode.StopListener;
import com.datalogic.decode.TimeoutListener;
import com.datalogic.decode.configuration.DisplayNotification;
import com.datalogic.device.ErrorManager;

public class MainScanActivity extends Activity implements ReadListener,
StartListener, TimeoutListener, StopListener {

	static final String TAG = "Test-Scanner";

	// It will display the result
	private EditText showScanResult;
	// Current scan engine status
	private TextView status;
	// The scan button
	private Button mScan;
	private int statusTextColor;

	private BarcodeManager mBarcodeManager;
	private boolean ignoreStop = false;
	private boolean previousNotification;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		// Enable exceptions
		ErrorManager.enableExceptions(true);

		setupView();
	}

	private void setupView() {
		showScanResult = (EditText) findViewById(R.id.scan_result);

		status = (TextView) findViewById(R.id.scanner_status);
		statusTextColor = status.getCurrentTextColor();

		mScan = (Button) findViewById(R.id.scan);
		mScan.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					try {
						mScan.setPressed(true);
						mBarcodeManager.startDecode();
					} catch (Exception e) {
						Log.e(TAG, "Action DOWN", e);
						showMessage("ERROR! Check logcat");
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					try {
						mBarcodeManager.stopDecode();
						mScan.setPressed(false);
					} catch (Exception e) {
						Log.e(TAG, "Action UP", e);
						showMessage("ERROR! Check logcat");
					}
					v.performClick();
				}
				return true;
			}
		}
				);
	}

	// Connects to the barcode manager
	private void initScan() {
		try {
			mBarcodeManager = new BarcodeManager();
		} catch (Exception e) {
			Log.e(TAG, "Error while creating BarcodeManager");
			showMessage("ERROR! Check logcat");
			finish();
			return;
		}

		// Disable Display Notification
		DisplayNotification dn = new DisplayNotification(mBarcodeManager);
		previousNotification = dn.enable.get();
		dn.enable.set(false);
		try {
			dn.store(mBarcodeManager, false);
		} catch (Exception e) {
			Log.e(TAG, "Cannot disable Display Notification", e);
		}

		registerListeners();
	}

	// Register this activity as a listener for several scanner events
	private void registerListeners() {
		try {
			mBarcodeManager.addReadListener(this);
			mBarcodeManager.addStartListener(this);
			mBarcodeManager.addStopListener(this);
			mBarcodeManager.addTimeoutListener(this);
		} catch (Exception e) {
			Log.e(TAG, "Cannot add listener, the app won't work");
			showMessage("ERROR! Check logcat");
			finish();
		}
	}

	// Unregister this activity as a listener
	private void releaseListeners() {
		try {
			mBarcodeManager.removeReadListener(this);
			mBarcodeManager.removeStartListener(this);
			mBarcodeManager.removeStopListener(this);
			mBarcodeManager.removeTimeoutListener(this);
		} catch (DecodeException e) {
			Log.e(TAG, "Cannot remove listeners, the app won't work", e);
			showMessage("ERROR! Check logcat");
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Release barcode manager
		try {
			if (mBarcodeManager != null) {
				mBarcodeManager.stopDecode();

				releaseListeners();

				// restore previous setting
				DisplayNotification dn = new DisplayNotification(mBarcodeManager);
				dn.enable.set(previousNotification);
				dn.store(mBarcodeManager, false);

				mBarcodeManager = null;
			}
		} catch (Exception e) {
			Log.e(TAG, "Cannot detach from Scanner correctly", e);
			showMessage("ERROR! Check logcat");
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initScan();
		showScanResult.setHint(getResources().getText(R.string.scanner_hint));
	}

	@Override
	public void onScanStarted() {
		status.setTextColor(Color.RED);
		status.setText("Scanning");

		showScanResult.setText("");
		showMessage("Scanner Started");
		Log.d(TAG, "Scan start");
	}

	@Override
	public void onRead(DecodeResult result) {
		status.setTextColor(Color.rgb(51, 153, 51));
		status.setText("Result");

		showScanResult.append("Barcode Type: " + result.getBarcodeID()+ "\n");
		String string = result.getText();
		if( string!= null) {
			showScanResult.append("Result: " + string);
		}
		ignoreStop = true;

		byte[] bData = result.getRawData();
		String bDataHex = encodeHex(bData);
		String text = result.getText();
		String symb = result.getBarcodeID().toString();

		// All data as log
		Log.d(TAG, "Scan read");
		Log.d(TAG, "Symb: " + symb);
		Log.d(TAG, "Data: " + text);
		Log.d(TAG, "Data[]: " + bData.toString());
		Log.d(TAG, "As hex: " + bDataHex);

		showMessage("Scanner Read");
	}

	@Override
	public void onScanStopped() {
		if(!ignoreStop) {
			status.setTextColor(statusTextColor);
			status.setText("Ready");
			showMessage("Scanner Stopped");
		} else {
			ignoreStop = false;
		}
	}

	@Override
	public void onScanTimeout() {
		status.setTextColor(Color.WHITE);
		status.setText("Timeout");
		ignoreStop = true;

		showMessage("Scanning timed out");
		Log.d(TAG, "Scan timeout");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_release_listeners:
			releaseListeners();
			return true;
		case R.id.action_register_listeners:
			registerListeners();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	String encodeHex(byte[] data) {
		if(data == null)
			return "";

		StringBuffer hexString = new StringBuffer();
		hexString.append('[');
		for (int i = 0; i < data.length; i++) {
			hexString.append(' ');
			String hex = Integer.toHexString(0xFF & data[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		hexString.append(']');
		return hexString.toString();
	}

	void showMessage(String s) {
		if (mToast == null || mToast.getView().getWindowVisibility() != View.VISIBLE) {
			mToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
			mToast.show();
		} else {
			mToast.setText(s);
		}
	}
}
