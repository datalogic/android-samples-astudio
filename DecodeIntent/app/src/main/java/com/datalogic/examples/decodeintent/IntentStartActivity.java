// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodeintent;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Log and Toast the received action, categories, barcode data and symboloty type.
 * MainActivity.isReading will be set to false.
 */
public class IntentStartActivity extends Activity {
	private TextView textMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_start);

		// Get the Intent that created this activity.
		Intent intent = getIntent();
		String action = intent.getAction();

		showMessage("Started IntentStartActivity");
		Log.d(getClass().getName(), "Started activity with Intent");

		Set<String> category_all = intent.getCategories();
		StringBuilder category = new StringBuilder();
		for (String s : category_all) {
			category.append(s);
		}

		// Which Barcode type?
		String type = intent.getStringExtra(IntentWedgeSample.EXTRA_TYPE);
		// What is the result?
		String data = intent.getStringExtra(IntentWedgeSample.EXTRA_DATA);

		textMsg = (TextView) findViewById(R.id.textResult);
		textMsg.append("Action: " + action + "\n" 
				+ "Category: " + category.toString() + "\n" 
				+ "Type: " + type + "\n" 
				+ "Data: " + data);
	}

	private void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
