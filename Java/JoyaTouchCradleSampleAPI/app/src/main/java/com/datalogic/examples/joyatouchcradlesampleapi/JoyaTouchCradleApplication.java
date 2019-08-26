// ©2016 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.joyatouchcradlesampleapi;

import com.datalogic.extension.selfshopping.cradle.joyatouch.CradleJoyaTouch;

import android.app.Application;

/**
 * Application context containing the objects shared by the activities.
 */
public class JoyaTouchCradleApplication extends Application
{
	/**
	 * CradleJoyaTouch instance shared by the sample activities.
	 */
	private CradleJoyaTouch jtCradle = null;
	
	public void setCradleJoyaTouch(CradleJoyaTouch jtCradle)
	{
		this.jtCradle = jtCradle;
	}
	
	public CradleJoyaTouch getCradleJoyaTouch()
	{
		return jtCradle;
	}
}
