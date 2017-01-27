// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.datalogic.examples.joyatouchcradlesampleapi.R;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;
import com.datalogic.extension.selfshopping.cradle.joyatouch.StateInfo;

/**
 * Activity for Cradle state information.
 */
public class GetCradleStateActivity extends Activity {

	private TextView textDeviceInCradle;
	private TextView textState;

	private CradleJoyaTouch jtCradle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cradle_state_info);

		JoyaTouchCradleApplication application = (JoyaTouchCradleApplication)getApplicationContext();
		jtCradle = application.getCradleJoyaTouch();
			
		textDeviceInCradle = (TextView)findViewById(R.id.textDeviceInCradle);
		textState = (TextView) findViewById(R.id.textState);
		
		if (savedInstanceState == null)
		{
			// Change displayed text.
			updateContent();
		}
	}

	/**
	 * Update showed TextViews with Cradle State Info if the device is into the Cradle, otherwise
	 * show an error message.
	 */
	public void updateContent()
	{
		boolean inCradle = jtCradle.isDeviceInCradle();
		if (inCradle)
		{
			StateInfo state = new StateInfo();
			if (jtCradle.getCradleState(state))
			{
				textDeviceInCradle.setTextColor(Color.BLUE);
				textDeviceInCradle.setText(getString(R.string.device_in_cradle));
				
				textState.setText("");
				textState.setText("Application version: " + state.getApplVersion() + "\n"
						+ "Bootloader version: " + state.getBtldrVersion() + "\n"
						+ "Insertion count: " + state.getInsertionCount() + "\n"
						+ "Slot index: " + state.getSlotIndex() + "\n"
						+ "Fast charge available: " + state.isFastChargeAvailable() + "\n");
			}
			else
			{
				textDeviceInCradle.setTextColor(Color.RED);
				textDeviceInCradle.setText(getString(R.string.get_cradle_state_failed));
				
				textState.setText("");
			}
		}
		else
		{
			textDeviceInCradle.setTextColor(Color.RED);
			textDeviceInCradle.setText(getString(R.string.device_not_in_cradle));
			
			textState.setText("");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.cradle_state_info_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.item_refresh:
	            updateContent();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
