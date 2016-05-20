// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.datalogic.device.info.SYSTEM;

/**
 * Activity for battery information.
 */
public class BatteryActivity extends Activity {

	private TextView txtPower;
	private Intent batteryStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);

		txtPower = (TextView) findViewById(R.id.txtPower);

		// Get Android's status.
		batteryStatus = registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		// Change displayed text.
		setText();
	}

	// updates showed TextView with battery infos.
	public void setText() {
		txtPower.setText("");
		txtPower.setText("Battery Info: \n" + getBatteryInfo() + "\n"
				+ "Battery Status: " + getStatus() + "\n"
				+ "External AC Power: " + getExtPowerStatus() + "\n"
				+ "External USB Power: " + getUsbPowerStatus() + "\n"
				+ "Current level: " + getCurrentLevel() + "\n"
				);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reset, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reset:
			setText();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	public String getBatteryInfo() {
		String output = "  capacity:" + SYSTEM.BATTERY_INFO.capacity + "\n"
				+ "  year:" + SYSTEM.BATTERY_INFO.year + "\n" 
				+ "  week:" + SYSTEM.BATTERY_INFO.week + "\n" 
				+ "  serial_number:" + SYSTEM.BATTERY_INFO.serialNumber + "\n" 
				+ "  manufacturer:" + SYSTEM.BATTERY_INFO.manufacturer + "\n";
		return output;
	}

	public boolean getExtPowerStatus() {
		int external_power_source = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
		return external_power_source == BatteryManager.BATTERY_PLUGGED_AC;
	}

	public boolean getUsbPowerStatus() {
		int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		return chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
	}

	public String getStatus() {
		String resultStatus;
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

		switch(status) {
		case BatteryManager.BATTERY_STATUS_CHARGING:
			resultStatus = "Charging";
			break;
		case BatteryManager.BATTERY_STATUS_DISCHARGING:
			resultStatus = "Discharging";
			break;
		case BatteryManager.BATTERY_STATUS_FULL:
			resultStatus = "Full";
			break;
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
			resultStatus = "Not charging";
			break;
		default:
			resultStatus = "Unknown";
			break;
		}

		return resultStatus;
	}

	public boolean getChargingStatus() {
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		return status == BatteryManager.BATTERY_STATUS_CHARGING;
	}

	public boolean getDischargingStatus() {
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		return status == BatteryManager.BATTERY_STATUS_DISCHARGING;
	}

	public float getCurrentLevel() {
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		return level / (float)scale;
	}
}
