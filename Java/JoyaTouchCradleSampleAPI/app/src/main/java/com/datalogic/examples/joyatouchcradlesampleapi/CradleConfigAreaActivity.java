// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.datalogic.examples.joyatouchcradlesampleapi.R;
import com.datalogic.extension.selfshopping.cradle.joyatouch.ConfigArea;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;

/**
 * Activity for Cradle config area read / write.
 */
public class CradleConfigAreaActivity extends Activity
{
	private byte[] configValues;
	
	private GridView grid;
	
	private CradleJoyaTouch jtCradle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cradle_config_area);

		JoyaTouchCradleApplication application = (JoyaTouchCradleApplication)getApplicationContext();
		jtCradle = application.getCradleJoyaTouch();
		
		if (savedInstanceState == null)
		{
			ConfigArea config = new ConfigArea();
			if (jtCradle.readConfigArea(config))
				configValues = config.getContent();
			else
				Toast.makeText(this, "Failure reading config area. Retry.", Toast.LENGTH_LONG).show();
		}
		else
		{
			configValues = savedInstanceState.getByteArray("configValues");
		}
		
		ConfigAreaAdapter adapter = new ConfigAreaAdapter(this, configValues);
		grid = (GridView)findViewById(R.id.configValuesGrid);
		grid.setAdapter(adapter);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putByteArray("configValues", configValues);
		super.onSaveInstanceState(outState);
	}
	
	public void readConfig(View view)
	{
		ConfigArea config = new ConfigArea();
		if (jtCradle.readConfigArea(config))
		{
			configValues = config.getContent();
			ConfigAreaAdapter adapter = new ConfigAreaAdapter(this, configValues);
			grid.setAdapter(adapter);
			grid.invalidate();
		}
		else
		{
			Toast.makeText(this, "Failure reading config area. Retry.", Toast.LENGTH_LONG).show();
		}
	}
	
	public void writeConfig(View view)
	{
		ConfigArea config = new ConfigArea(configValues);
		if (jtCradle.writeConfigArea(config))
			Toast.makeText(this, "Config data written successfully.", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, "Failure writing config area. Retry.", Toast.LENGTH_LONG).show();
	}
}
