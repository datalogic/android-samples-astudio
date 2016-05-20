// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.datalogic.device.info.SYSTEM;

import java.util.Set;

/**
 * InfoActivity displays information about the device being used.
 */
public class InfoActivity extends Activity {

	// It will show device associated icon.
	private ImageButton btnBg;
	// Containing device infos.
	private TextView txtInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		txtInfo = (TextView) findViewById(R.id.txtInfo);
		btnBg = (ImageButton) findViewById(R.id.btnBg);
		
		getInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reset, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ( item.getItemId() == R.id.action_reset ) {
			getInfo();
			btnBg.setImageResource(R.drawable.ic_launcher);
			return true ;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Shows device information.
	 */
	public void getInfo() {
		txtInfo.setText(getDescription());
	}

	/**
	 * Activated by btnBg.  Displays DEVICE_IMAGE in btnBg.
	 */
	public void btnBgOnClick(View v) {
		Bitmap img = SYSTEM.DEVICE_IMAGE;
		float ratio = ((float)img.getHeight()) / ((float)img.getWidth());
		int width = 200;
		int height = (int) Math.ceil(width * ratio);
		btnBg.setImageBitmap(
				Bitmap.createScaledBitmap(img, width, height, true)
		);
	}

	public String getSDKVersion() {
		return SYSTEM.SDK_VERSION;
	}
	
	public String getServiceVersion() {
		return SYSTEM.SERVICES_VERSION;
	}

	public String getScannerType() {
		return SYSTEM.BARCODE_SCANNER_TYPE.toString();
	}

	public String getDescription() {
		StringBuilder builder = new StringBuilder(
		        "Scanner Type: " + getScannerType() + "\n"
				+ "Boot Type: " + SYSTEM.BOOT_TYPE.toString() + "\n"
				+ "Device Model: " + android.os.Build.MODEL + "\n"
				+ "WiFi type: " + SYSTEM.WIFI_TYPE.toString() + "\n"
				+ "Firmware Version: " + SYSTEM.getVersions().get("FIRMWARE") + "\n"
				+ "Kernel Version: " + SYSTEM.getVersions().get("KERNEL") + "\n");

		// Retrieve other info via Intent
		IntentFilter ifilter = new IntentFilter(com.datalogic.device.info.SYSTEM.Version.ACTION_DEVICE_INFO);
		Intent info = this.registerReceiver(null, ifilter);
		Bundle b = info.getExtras();
		Set<String> bundleStrings = b.keySet();
		Object tmp;

		for(String st : bundleStrings) {
			builder.append(st + ": ");
			tmp = b.get(st);
			if (tmp != null && tmp instanceof Integer) {
				builder.append((Integer) tmp);
			} else if(tmp != null && tmp instanceof String) {
				builder.append((String) tmp);
			} else {
				continue;
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
