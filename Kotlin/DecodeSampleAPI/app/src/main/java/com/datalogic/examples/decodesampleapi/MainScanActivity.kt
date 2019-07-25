// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.decodesampleapi

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.datalogic.decode.BarcodeManager
import com.datalogic.decode.DecodeException
import com.datalogic.decode.DecodeResult
import com.datalogic.decode.ReadListener
import com.datalogic.decode.StartListener
import com.datalogic.decode.StopListener
import com.datalogic.decode.TimeoutListener
import com.datalogic.decode.configuration.DisplayNotification
import com.datalogic.device.ErrorManager

class MainScanActivity : Activity(), ReadListener, StartListener, TimeoutListener, StopListener {

    // It will display the result
    private var showScanResult: EditText? = null
    // Current scan engine status
    private var status: TextView? = null
    // The scan button
    private var mScan: Button? = null
    private var statusTextColor: Int = 0

    private var mBarcodeManager: BarcodeManager? = null
    private var ignoreStop = false
    private var previousNotification: Boolean = false
    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)

        // Enable exceptions
        ErrorManager.enableExceptions(true)

        setupView()
    }

    private fun setupView() {
        showScanResult = findViewById<EditText>(R.id.scan_result)

        status = findViewById<TextView>(R.id.scanner_status)
        statusTextColor = status!!.currentTextColor

        mScan = findViewById<Button>(R.id.scan)
        mScan!!.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                try {
                    mScan!!.isPressed = true
                    mBarcodeManager!!.startDecode()
                } catch (e: Exception) {
                    Log.e(TAG, "Action DOWN", e)
                    showMessage("ERROR! Check logcat")
                }

            } else if (event.action == MotionEvent.ACTION_UP) {
                try {
                    mBarcodeManager!!.stopDecode()
                    mScan!!.isPressed = false
                } catch (e: Exception) {
                    Log.e(TAG, "Action UP", e)
                    showMessage("ERROR! Check logcat")
                }

                v.performClick()
            }
            true
        }
    }

    // Connects to the barcode manager
    private fun initScan() {
        try {
            mBarcodeManager = BarcodeManager()
        } catch (e: Exception) {
            Log.e(TAG, "Error while creating BarcodeManager")
            showMessage("ERROR! Check logcat")
            finish()
            return
        }

        // Disable Display Notification
        val dn = DisplayNotification(mBarcodeManager)
        previousNotification = dn.enable.get()
        dn.enable.set(false)
        try {
            dn.store(mBarcodeManager, false)
        } catch (e: Exception) {
            Log.e(TAG, "Cannot disable Display Notification", e)
        }

        registerListeners()
    }

    // Register this activity as a listener for several scanner events
    private fun registerListeners() {
        try {
            mBarcodeManager!!.addReadListener(this)
            mBarcodeManager!!.addStartListener(this)
            mBarcodeManager!!.addStopListener(this)
            mBarcodeManager!!.addTimeoutListener(this)
        } catch (e: Exception) {
            Log.e(TAG, "Cannot add listener, the app won't work")
            showMessage("ERROR! Check logcat")
            finish()
        }

    }

    // Unregister this activity as a listener
    private fun releaseListeners() {
        try {
            mBarcodeManager!!.removeReadListener(this)
            mBarcodeManager!!.removeStartListener(this)
            mBarcodeManager!!.removeStopListener(this)
            mBarcodeManager!!.removeTimeoutListener(this)
        } catch (e: DecodeException) {
            Log.e(TAG, "Cannot remove listeners, the app won't work", e)
            showMessage("ERROR! Check logcat")
            finish()
        }

    }

    override fun onPause() {
        super.onPause()

        // Release barcode manager
        try {
            if (mBarcodeManager != null) {
                mBarcodeManager!!.stopDecode()

                releaseListeners()

                // restore previous setting
                val dn = DisplayNotification(mBarcodeManager)
                dn.enable.set(previousNotification)
                dn.store(mBarcodeManager, false)

                mBarcodeManager = null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Cannot detach from Scanner correctly", e)
            showMessage("ERROR! Check logcat")
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        initScan()
        showScanResult!!.hint = resources.getText(R.string.scanner_hint)
    }

    override fun onScanStarted() {
        status!!.setTextColor(Color.RED)
        status!!.text = "Scanning"

        showScanResult!!.setText("")
        showMessage("Scanner Started")
        Log.d(TAG, "Scan start")
    }

    override fun onRead(result: DecodeResult) {
        status!!.setTextColor(Color.rgb(51, 153, 51))
        status!!.text = "Result"

        showScanResult!!.append("Barcode Type: " + result.getBarcodeID() + "\n")
        val string = result.getText()
        if (string != null) {
            showScanResult!!.append("Result: " + string!!)
        }
        ignoreStop = true

        // Convert to IntArray
        val bData = result.rawData
        var iData = IntArray(0)
        for(x in bData){
            iData += x.toInt()
        }

        val bDataHex = encodeHex(iData)
        val text = result.text
        val symb = result.barcodeID.toString()

        // All data as log
        Log.d(TAG, "Scan read")
        Log.d(TAG, "Symb: $symb")
        Log.d(TAG, "Data: $text")
        Log.d(TAG, "Data[]: $bData")
        Log.d(TAG, "As hex: $bDataHex")

        showMessage("Scanner Read")
    }

    override fun onScanStopped() {
        if (!ignoreStop) {
            status!!.setTextColor(statusTextColor)
            status!!.text = "Ready"
            showMessage("Scanner Stopped")
        } else {
            ignoreStop = false
        }
    }

    override fun onScanTimeout() {
        status!!.setTextColor(Color.WHITE)
        status!!.text = "Timeout"
        ignoreStop = true

        showMessage("Scanning timed out")
        Log.d(TAG, "Scan timeout")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_release_listeners -> {
                releaseListeners()
                return true
            }
            R.id.action_register_listeners -> {
                registerListeners()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // Changed typeof data from ByteArray
    internal fun encodeHex(data: IntArray?): String {
        if (data == null)
            return ""

        val hexString = StringBuffer()
        hexString.append('[')
        for (i in data.indices) {
            hexString.append(' ')
            val hex = Integer.toHexString(0xFF and data[i])
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        hexString.append(']')
        return hexString.toString()
    }

    internal fun showMessage(s: String) {
        if (mToast == null || mToast!!.view.windowVisibility != View.VISIBLE) {
            mToast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
            mToast!!.show()
        } else {
            mToast!!.setText(s)
        }
    }

    companion object {

        internal val TAG = "Test-Scanner"
    }
}


