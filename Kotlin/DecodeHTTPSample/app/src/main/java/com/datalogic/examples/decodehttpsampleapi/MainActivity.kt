package com.datalogic.examples.decodehttpsampleapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.datalogic.examples.decodehttpsampleapi.R
import kotlinx.android.synthetic.main.activity_main.web_view as web_view
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myWebView = web_view as WebView
        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
        // Just load web page with all the control logic
        myWebView.loadUrl("file:///android_asset/test.html")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

}
