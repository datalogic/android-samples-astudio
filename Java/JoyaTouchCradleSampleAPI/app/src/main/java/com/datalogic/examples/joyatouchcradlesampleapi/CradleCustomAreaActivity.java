// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.datalogic.examples.joyatouchcradlesampleapi.R;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;
import com.datalogic.extension.selfshopping.cradle.joyatouch.CustomArea;

/**
 * Activity for Cradle custom area read / write.
 */
public class CradleCustomAreaActivity extends Activity
{
	private byte[] customValues;
	
	private EditText text;
	
	private CradleJoyaTouch jtCradle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cradle_custom_area);
		
		text = (EditText)findViewById(R.id.customValuesText);

		JoyaTouchCradleApplication application = (JoyaTouchCradleApplication)getApplicationContext();
		jtCradle = application.getCradleJoyaTouch();
		
		if (savedInstanceState == null)
		{
			CustomArea custom = new CustomArea();
			if (jtCradle.readCustomArea(custom, CustomArea.SIZE))
			{
				customValues = custom.getContent();
				setTextUTF();
			}
			else
			{
				Toast.makeText(this, "Failure reading custom area. Retry.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	void setTextUTF()
	{
		// Count the number of bytes before finding a 0
		int numBytes = 0;
		for (int i = 0; i < customValues.length; i++)
		{
			if (customValues[i] == (byte)0)
			{
				numBytes = i;
				break;
			}
		}
		
		if (numBytes != 0)
		{
			byte customValuesValid[] = new byte[numBytes];
			System.arraycopy(customValues, 0, customValuesValid, 0, numBytes);
			try
			{
				String utfValues = new String(customValuesValid, "UTF-8");
				text.setText(utfValues);
			}
			catch (UnsupportedEncodingException e)
			{
				Toast.makeText(this, "Wrong conversion of the custom area bytes into a UTF-8 string.", 
						Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(this, "Custom area contains no bytes before the first null value.", 
					Toast.LENGTH_LONG).show();
		}
	}
	
	public void readCustom(View view)
	{
		CustomArea custom = new CustomArea();
		if (jtCradle.readCustomArea(custom, CustomArea.SIZE))
		{
			customValues = custom.getContent();
			setTextUTF();
		}
		else
		{
			Toast.makeText(this, "Failure reading custom area. Retry.", Toast.LENGTH_LONG).show();
		}
	}
	
	public void clearCustom(View view)
	{
		CustomArea custom = new CustomArea();
		customValues = custom.getContent();
		for (int i = 0; i < customValues.length; i++)
			customValues[i] = 0;
		if (jtCradle.writeCustomArea(custom, custom.getSize()))
		{
			text.setText("");
		}
		else
		{
			Toast.makeText(this, "Failure clearing custom area. Retry.", Toast.LENGTH_LONG).show();
		}
	}
	
	public void writeCustom(View view)
	{
		try
		{
			customValues = text.getText().toString().getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			Toast.makeText(this, "Wrong conversion of the UTF-8 string into custom area bytes.", 
					Toast.LENGTH_LONG).show();
			return;
		}
		
		if (customValues == null || customValues.length == 0 || customValues.length > CustomArea.SIZE)
		{
			Toast.makeText(this, "Invalid custom area bytes size.", Toast.LENGTH_LONG).show();
			return;
		}
		
		CustomArea custom = new CustomArea(customValues);
		if (jtCradle.writeCustomArea(custom, custom.getSize()))
			Toast.makeText(this, "Custom data written successfully.", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, "Failure writing custom area. Retry.", Toast.LENGTH_LONG).show();
	}
}
