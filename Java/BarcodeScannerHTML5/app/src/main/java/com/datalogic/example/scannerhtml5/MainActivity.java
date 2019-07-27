package com.datalogic.example.scannerhtml5;

import java.io.File;
import java.net.URISyntaxException;

import android.os.Build;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.decode.StartListener;
import com.datalogic.decode.StopListener;
import com.datalogic.decode.TimeoutListener;
import com.datalogic.decode.configuration.DisplayNotification;
import com.datalogic.decode.configuration.IntentWedge;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


/*
 * Example of webkit application able to start decoding and able to access to decoder
 * configuartion from a local or remote html page
 * this example uses a local html page contained by the apk itself under the /assets directory 
 */
public class MainActivity extends Activity implements ReadListener, StartListener,
	StopListener, TimeoutListener {
	
	private final static String TAG = "BarcodeScanner";
	private final static boolean USE_INTENT = false;

	BarcodeManager barcodeManager = null;
	boolean wedgeWasEnabled = false;
	boolean toastWasEnabled = false;
	
    WebView mWebView;
    WebSettings mWebViewSettings;
    private static String default_url = "file:///android_asset/www/index.html";
    private final MainActivity app = this;
    Settings webSettings;
    long scanStartTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		try {
			if (barcodeManager == null) {
				barcodeManager = new BarcodeManager();
			}
		} catch (DecodeException e) {
			e.printStackTrace();
		}

		mWebView = (WebView) findViewById(R.id.webView_main);
		setupWebView();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void setupWebView() {
        /*
         * This method is invoked when a link is clicked, the application 
         * forward the URL to Android, so the link is shown inside a web browser
         * instead showing it inside the app webview.
         * 
         * This is also used for handling URIs representing Android intents
         */
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	try {
					Intent i = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
					startActivity(i);
					return true;
				} catch (URISyntaxException e) {
					e.printStackTrace();
					Log.w(TAG, "Bad intent URI");
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				}
            	return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            	if (!url.matches("file://"+getPageUrl("index"))) {
            		webSettings.setOpen(false);
            	}
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            	app.onPageComplete();
            }
        });

		// Avoid text selection on long-click
        mWebView.setLongClickable(false);
		mWebView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});

		// Disable haptic feedback (vibration) on long-click
		mWebView.setHapticFeedbackEnabled(false);

		mWebView.clearCache(true);
        
		mWebViewSettings = mWebView.getSettings();
		
		// Setup HTML5 Local/Session Storage and DB support
		mWebViewSettings.setDomStorageEnabled(true);
		mWebViewSettings.setDatabaseEnabled(true);

		// Setup HTML5 GeoLocation
		mWebViewSettings.setGeolocationDatabasePath(getFilesDir().getPath() +
				mWebView.getContext().getPackageName() + "/geo/");
		mWebViewSettings.setGeolocationEnabled(true);
		
		// Disable zoom on double tap
		mWebViewSettings.setSupportZoom(false);
		
		mWebViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        
        mWebView.setWebChromeClient(new WebChromeClient() {
        	
        	/*
        	 * This method allows us to capture the JS alert event and run
        	 * our own code, in this case showing a native Alert dialog
        	 */
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                    final android.webkit.JsResult result) {
            	
                new AlertDialog.Builder(app)
                        .setTitle("Javascript Dialog")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            /*
             * Always grant geo-location permission 
             */
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
            		GeolocationPermissions.Callback callback) {
            	Log.d("GeoLoc", "Permission for: " + origin);
            	callback.invoke(origin, true, false);
            }
        });
        
        webSettings = new Settings(this);
        
        // Export an interface for Utils/Settings
		mWebView.addJavascriptInterface(webSettings, "MainSettings");

		// JavaScript bridge to Datalogic BarcodeManager class
		mWebView.addJavascriptInterface(new BarcodeManagerWrapper(), "BarcodeManager");
		
		// Finally enable execution of JS inside the WebView
		mWebViewSettings.setJavaScriptEnabled(true);
		
    	mWebView.loadUrl(default_url);
	}
	
	protected void onPageComplete() {
		mWebView.setInitialScale(0);
		mWebViewSettings.setLoadWithOverviewMode(true);
		mWebViewSettings.setUseWideViewPort(false);
	}

	/**
	 *	Wrapper around BarcodeManager class to allow the usage of either intents
	 *	or SDK calls to access the scanner
	 */
	public class BarcodeManagerWrapper {
        @JavascriptInterface
		public void startDecode(int timeout) {
			if (USE_INTENT) {
				Intent myintent = new Intent();
				myintent.setAction(BarcodeManager.ACTION_START_DECODE);
				sendBroadcast(myintent);
				onScanStarted();
			} else {
				barcodeManager.startDecode(timeout);
			}
		}

        @JavascriptInterface
		public void stopDecode() {
			if (USE_INTENT) {
				Intent myintent = new Intent();
				myintent.setAction(BarcodeManager.ACTION_STOP_DECODE);
				sendBroadcast(myintent);
				onScanStopped();
			} else {
				barcodeManager.stopDecode();
			}
		}
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == R.id.item_about){
			Utils.callJsFunction(mWebView, "showSettings");
		}
		return false;
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			barcodeManager.addReadListener(this);
			barcodeManager.addStartListener(this);
			barcodeManager.addStopListener(this);
			barcodeManager.addTimeoutListener(this);

			DisplayNotification toast = new DisplayNotification(barcodeManager);
			toastWasEnabled = toast.enable.get();
			if (toastWasEnabled) {
				toast.enable.set(false);
				toast.store(barcodeManager, false);
			}
			
			IntentWedge wedge = new IntentWedge(barcodeManager);
			wedgeWasEnabled = wedge.enable.get();
			if (wedgeWasEnabled) {
				wedge.enable.set(false);
				wedge.store(barcodeManager, false);
			}
		} catch (DecodeException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (barcodeManager != null) {
			if (toastWasEnabled) {
				DisplayNotification toast = new DisplayNotification(barcodeManager);
				toast.enable.set(true);
				toast.store(barcodeManager, false);
			}
			
			if (wedgeWasEnabled) {
				IntentWedge wedge = new IntentWedge(barcodeManager);
				wedge.enable.set(true);
				wedge.store(barcodeManager, false);
			}
			
			try {
				barcodeManager.release();
			} catch (DecodeException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (barcodeManager != null) {
			try {
				barcodeManager.release();
				barcodeManager = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    /*
	     *  Handle the back button to close the settings modal and 
	     *  go back in the webview's history, in case the user moved
	     *  to another page.
	     */
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (webSettings.getOpen()) {
	    		Utils.evalJs(mWebView, "closeModal()");
	    		webSettings.setOpen(false);
	    		return true;
	    	}
	    	
	    	if (mWebView.canGoBack()) {
	    		File page =  new File(getPageUrl("index"));
	    		String home;
	    		if (page.exists()) {
	    			home = "file://"+page.getAbsolutePath();
	    		} else {
	    			Log.d(TAG, "Loading default page. "+page.getAbsolutePath()+" not found");
	    			home = default_url;
	    		}
	    		
	    		WebBackForwardList stack = mWebView.copyBackForwardList();
	    		try {
					if (stack.getCurrentItem().getUrl().equals(home))
						return false;
				}
	    		catch(NullPointerException n){
					Log.d(TAG, "Getting URL for backForwardList, URL not found");
				}

	    		mWebView.loadUrl(home);
	    		mWebView.clearHistory();
	    		return true;
	    	}
	    }
	    
	    /*
	     *  If it wasn't the Back key or there's no web page history, bubble up 
	     *  the event to the default system behavior (probably exit the activity)
	     */
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onScanStarted() {
		Log.d(TAG, "onStart");
		scanStartTime = System.currentTimeMillis();
		Utils.callJsFunction(mWebView, "onStart");
	}

	@Override
	public void onScanTimeout() {
		Log.d(TAG, "onTimeout");
		Utils.callJsFunction(mWebView, "onTimeout");
	}

	@Override
	public void onScanStopped() {
		Log.d(TAG, "onStop" );
		Utils.callJsFunction(mWebView, "onStop");
	}

	@Override
	public void onRead(DecodeResult decodeResult) {
		Log.d(TAG, "onRead: " + decodeResult.getText());
		Utils.callJsFunction(mWebView, "onRead",
				decodeResult.getText().trim(),
				decodeResult.getBarcodeID().name(),
				String.valueOf(System.currentTimeMillis() - scanStartTime));
	}

	public String getPageUrl(String page) {
		return getWwwRoot() + page + ".html";
	}
	
	public String getWwwRoot() {
		return Environment.getExternalStorageDirectory().toString() + "/html5demo/";
	}
}