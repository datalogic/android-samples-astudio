package com.datalogic.example.scannerhtml5;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class Settings {
	private Context context;
	private boolean open = false;
	private MediaPlayer mediaPlayer;
	
	public Settings(Context context) {
		this.context = context;
	}

	@JavascriptInterface
	public String getAppVersion() {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@JavascriptInterface
	public String getSoundUri() {
		return "file:///system/media/audio/notifications/Argon.ogg";
	}

	@JavascriptInterface
	public String getModel() {
		return Build.MODEL;
	}

	@JavascriptInterface
	public String getDevice() {
		return Build.DEVICE;
	}

	@JavascriptInterface
	public void play() {
		mediaPlayer.start();
	}

	@JavascriptInterface
	public String getRoot() {
		return null;//getWwwRoot();
	}

	@JavascriptInterface
	public void setOpen(boolean open) {
		this.open = open;
	}

	@JavascriptInterface
	public boolean getOpen() {
		return open;
	}
}