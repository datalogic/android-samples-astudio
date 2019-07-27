package com.datalogic.example.scannerhtml5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

public class Utils {
	private final static String TAG = "Utils";
	
	/**
	 * Call a JS script function within the webview
	 * 
	 * @param w	The WebView
	 * @param name JS function name to be defined in any JS script
	 * @param args Arguments to the function, they will be passed as
	 * 	strings
	 */
	public static void callJsFunction(WebView w, String name, String... args) {
		StringBuilder js = new StringBuilder();
		
		/*
		 * Build JS code to call a function, something like:
		 * 	myFunction("arg1", null, "arg3")
		 */
		for (int i=0; i<args.length; i++) {
			if (args[i] == null) {
				js.append("null");
			} else {
				// [BugFix] QC#79: Escape single quotes in scanned data otherwise it
				// messes up when evaluating JS code
				js.append("'"+args[i].replaceAll("'", "\\\\'")+"'");
			}
			if (i != args.length-1) js.append(",");
		}
		js.insert(0, name + "(");
		js.append(")");
		
		//Log.d(TAG, "JS eval: " + js.toString());
		evalJs(w, js.toString());
	}
	
	/**
	 * Evaluate any JS code inside the WebView
	 * 
	 * @param w The WebView
	 * @param js JS code
	 */
	public static void evalJs(WebView w, String js) {
		w.loadUrl("javascript:" + js);
	}
	
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static void copyToInternalStorage(Context context,
			ArrayList<String> destFiles) {
		File destFile;
		for (int i = 0; i < destFiles.size(); i++) {
			
			destFile = new File(context.getFilesDir().getAbsolutePath()
					+ File.separator + destFiles.get(i));
			
			if (!destFile.exists()) {
				Log.d(TAG, "Creating: " + destFile.getAbsolutePath());
				destFile.mkdirs();
				
				try {
					copyFromAssetsToStorage(context, "www/" + destFiles.get(i),
							destFiles.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Log.d(TAG, "File exists: " + destFile.getAbsolutePath());
			}
		}
	}

	private static void copyFromAssetsToStorage(Context context, String src,
			String dst) throws IOException {
		InputStream is = context.getAssets().open(src);
		OutputStream os = new FileOutputStream(dst);
		
		copyStream(is, os);
		
		os.flush();
		os.close();
		is.close();
	}

	private static void copyStream(InputStream Input, OutputStream Output)
			throws IOException {
		byte[] buffer = new byte[5120];
		int length = Input.read(buffer);
		while (length > 0) {
			Output.write(buffer, 0, length);
			length = Input.read(buffer);
		}
	}
}
