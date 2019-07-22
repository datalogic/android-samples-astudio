// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.datalogic.device.DeviceException;
import com.datalogic.device.ErrorManager;

/**
 * Activity to enable/disable Nfc
 */
public class NfcActivity extends Activity {

	private TextView nfcStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);

		nfcStatus = (TextView) findViewById(R.id.nfcStatus);

		boolean newVal;
		newVal = isNfcEnabled();
		nfcStatus.setText("Nfc is " + (newVal ? "" : "not ") + "enabled");
	}

	/**
	 * @return True if Nfc is enabled, false otherwise.
	 */
	public boolean isNfcEnabled() {
		android.nfc.NfcManager manager = (android.nfc.NfcManager) getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = manager.getDefaultAdapter();
		if (adapter != null && adapter.isEnabled())
			return true;
		return false;
	}

	/**
	 * Start NFC settings activity. 
	 */
	public void btnSettingsOnClick(View v) {
        Intent viewIntent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(viewIntent);
	}

	public void btnSwitchNFC(View v) {
		boolean newVal = !isNfcEnabled();
		setEnableNfc(newVal);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		newVal = isNfcEnabled();
		nfcStatus.setText("Nfc is " + (newVal ? "" : "not ") + "enabled");
	}

	/**
	 * Enable or disable Nfc, using com.datalogic.device.nfc.NfcManager.
	 */
	public void setEnableNfc(boolean enable) {
		boolean previous = ErrorManager.areExceptionsEnabled();
		ErrorManager.enableExceptions(false);
		ErrorManager.clearErrors();

		int error = new com.datalogic.device.nfc.NfcManager().enableNfcAdapter(enable);
		if(error != DeviceException.SUCCESS) {
			Log.e(getClass().getName(), "Error while setting NFC", ErrorManager.getLastError());
		}
		ErrorManager.enableExceptions(previous);
	}

}
