// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.datalogic.device.DeviceException;
import com.datalogic.device.ErrorManager;
import com.datalogic.device.location.LocationMode;
import com.datalogic.device.location.LocationManager;
/**
 * Switches on/off GPS+Network location.
 */
public class LocationActivity extends Activity {

	// Displays current gps status: if on or off.
	private TextView gpsStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		gpsStatus = (TextView) findViewById(R.id.gpsStatus);
		
		boolean newVal;
		newVal = isGPSEnabled();
		gpsStatus.setText("GPS is " + (newVal ? "" : "not ") + "enabled");
	}

	/**
	 * Activated by btnGps. Sets GPS to the opposite state it is currently in.
	 */
	public void btnGpsOnClick(View v) {
		boolean newVal = !isGPSEnabled();
		setGPSState(newVal);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// It should not fail
			Log.e(getClass().getName(), "Error during sleep", e);
		}
		newVal = isGPSEnabled();
		gpsStatus.setText("GPS is " + (newVal ? "" : "not ") + "enabled");
	}

	/**
	 * Start Location settings activity. 
	 */
	public void btnSettingsOnClick(View v) {
        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(viewIntent);
	}

	/**
	 * Use android.location.LocationManager to determine if GPS is enabled.
	 */
	public boolean isGPSEnabled() {
		android.location.LocationManager loc = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return loc.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
	}

	/**
	 * Use LocationManager to set the gps as enabled (true), or disabled
	 * (false).
	 */
	public void setGPSState(boolean enable) {
		LocationManager gps = null;

		// Store previous exception preference.
		boolean previous = ErrorManager.areExceptionsEnabled();

		// We want to be notified through an exception if something goes wrong.
		ErrorManager.enableExceptions(true);
		try {
			gps = new LocationManager();
			gps.setLocationMode(enable ? LocationMode.SENSORS_AND_NETWORK : LocationMode.OFF);
		} catch (DeviceException e1) {
			// Just in case we get an error.
			Log.e(getClass().getName(), "Exception while switching location mode ", e1);
		}
		// Set previous value.
		ErrorManager.enableExceptions(previous);
	}

}
