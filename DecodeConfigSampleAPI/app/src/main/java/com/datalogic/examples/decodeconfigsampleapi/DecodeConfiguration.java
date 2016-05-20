// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodeconfigsampleapi;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.configuration.LengthControlMode;
import com.datalogic.decode.configuration.ScannerProperties;
import com.datalogic.device.configuration.ConfigException;
import com.datalogic.device.ErrorManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DecodeConfiguration extends Activity {

	// Constant for log messages.
	private final String LOGTAG = getClass().getName();

	BarcodeManager manager = null;
	ScannerProperties configuration = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create a BarcodeManager.
		manager = new BarcodeManager();

		// Pass it to ScannerProperties class.
		// ScannerProperties cannot be instantiated directly, instead call edit.
		configuration = ScannerProperties.edit(manager);

		// Now we can change some Scanner/Device configuration parameters.
		// These values are not applied, as long as store method is not called.
		configuration.code39.enable.set(true);
		configuration.code39.enableChecksum.set(false);
		configuration.code39.fullAscii.set(true);
		configuration.code39.Length1.set(20);
		configuration.code39.Length2.set(2);
		configuration.code39.lengthMode.set(LengthControlMode.TWO_FIXED);
		configuration.code39.sendChecksum.set(false);
		configuration.code39.userID.set('x');

		configuration.code128.enable.set(true);
		configuration.code128.Length1.set(6);
		configuration.code128.Length2.set(2);
		configuration.code128.lengthMode.set(LengthControlMode.RANGE);
		configuration.code128.userID.set('y');

		if (configuration.qrCode.isSupported()) {
			configuration.qrCode.enable.set(false);
		}

		// Change IntentWedge action and category to specific ones.
		configuration.intentWedge.action.set("com.datalogic.examples.decode_action");
		configuration.intentWedge.category.set("com.datalogic.examples.decode_category");

		// From here on, we would like to get a return value instead of an exception in case of error.
		ErrorManager.enableExceptions(false);

		// Now we are ready to store them.
		// Second parameter set to true saves configuration in a permanent way.
		// After boot settings will be still valid.
		int errorCode = configuration.store(manager, true);

		// Check return value.
		if(errorCode != ConfigException.SUCCESS) {
			Log.e(LOGTAG, "Error during store", ErrorManager.getLastError());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle menu item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			startSettingsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Method called by displayed button
	public void buttonClicked(View v) {
		startSettingsActivity();
	}

	private void startSettingsActivity() {
		// Create and start an intent to pop up Android Settings
		Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(dialogIntent);
	}
}
