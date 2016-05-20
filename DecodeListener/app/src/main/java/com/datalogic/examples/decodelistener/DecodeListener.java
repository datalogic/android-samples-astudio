// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodelistener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.device.ErrorManager;

public class DecodeListener extends Activity {

	private final String LOGTAG = getClass().getName();

	BarcodeManager decoder = null;
	ReadListener listener = null;
	TextView mBarcodeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Retrieve the TextView from the displayed layout.
		mBarcodeText = (TextView) findViewById(R.id.editText1);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i(LOGTAG, "onResume");

		// If the decoder instance is null, create it.
		if (decoder == null) { // Remember an onPause call will set it to null.
			decoder = new BarcodeManager();
		}

		// From here on, we want to be notified with exceptions in case of errors.
		ErrorManager.enableExceptions(true);

		try {

			// Create an anonymous class.
			listener = new ReadListener() {

				// Implement the callback method.
				@Override
				public void onRead(DecodeResult decodeResult) {
					// Change the displayed text to the current received result.
					mBarcodeText.setText(decodeResult.getText());
				}

			};

			// Remember to add it, as a listener.
			decoder.addReadListener(listener);

		} catch (DecodeException e) {
			Log.e(LOGTAG, "Error while trying to bind a listener to BarcodeManager", e);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		Log.i(LOGTAG, "onPause");

		// If we have an instance of BarcodeManager.
		if (decoder != null) {
			try {
				// Unregister our listener from it and free resources.
				decoder.removeReadListener(listener);

				// Let the garbage collector take care of our reference.
				decoder = null;
			} catch (Exception e) {
				Log.e(LOGTAG, "Error while trying to remove a listener from BarcodeManager", e);
			}
		}
	}
}
