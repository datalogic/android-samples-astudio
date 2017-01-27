// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import com.datalogic.examples.joyatouchcradlesampleapi.R;
import com.datalogic.extension.selfshopping.cradle.Cradle;
import com.datalogic.extension.selfshopping.cradle.CradleManager;
import com.datalogic.extension.selfshopping.cradle.CradleType;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * JoyaTouchCradleSampleAPI Launcher activity.
 */
public class JoyaTouchCradleAPITest extends Activity
{
	private final static String[] ACT_NAMES =
	{
		"Get Cradle State",
		"Control LED",
		"Control Lock",
		"Read / Write Config Area",
		"Read / Write Custom Area",
		"Reset"
	};

	private final static Class<?>[] ACT_CLASSES =
	{
		GetCradleStateActivity.class,
		ControlCradleLEDActivity.class,
		ControlCradleLockActivity.class,
		CradleConfigAreaActivity.class,
		CradleCustomAreaActivity.class,
		null
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Fill the Adapter with the available activity names/classes
		ListView listMainActivities = (ListView) findViewById(R.id.listMainActivities);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ACT_NAMES);
		listMainActivities.setAdapter(adapter);
		listMainActivities.setOnItemClickListener(new OpenActivityListener());
		
		if (savedInstanceState == null)
		{
			// Get the JoyaTouchCradle instance
			Cradle cradle = CradleManager.getCradle();
			if (cradle != null && cradle.getType() == CradleType.JOYA_TOUCH_CRADLE)
			{
				JoyaTouchCradleApplication application = (JoyaTouchCradleApplication)getApplicationContext();
				application.setCradleJoyaTouch((CradleJoyaTouch)cradle);
			}
			else
			{
				Toast.makeText(this, "JoyaTouchCradle not found. Cannot execute samples.", 
						Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}

	/**
	 * Open the activity of corresponding position.
	 * The position parameter is the index in ACT_CLASSES.
	 */
	public class OpenActivityListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			JoyaTouchCradleApplication application = (JoyaTouchCradleApplication)getApplicationContext();
			CradleJoyaTouch jtCradle = application.getCradleJoyaTouch();
			if (jtCradle.isDeviceInCradle())
			{
				// Execute the reset without calling any activity
				if (ACT_NAMES[position].equals("Reset"))
				{
					jtCradle.reset();
				}
				else
				{
					Intent intent = new Intent(JoyaTouchCradleAPITest.this, ACT_CLASSES[position]);
					startActivity(intent);
				}
			}
			else
			{
				Toast.makeText(JoyaTouchCradleAPITest.this, "Device is not in Cradle. Retry after insertion.", 
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
