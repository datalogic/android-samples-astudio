// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi

import android.app.Activity
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.datalogic.device.info.SYSTEM
import kotlin.math.ceil

/**
 * InfoActivity displays information about the device being used.
 */
class InfoActivity : Activity() {

    // It will show device associated icon.
    private var btnBg: ImageButton? = null
    // Containing device infos.
    private var txtInfo: TextView? = null


    val sdkVersion: String
        get() = SYSTEM.SDK_VERSION

    val serviceVersion: String
        get() = SYSTEM.SERVICES_VERSION

    private val scannerType: String
        get() = SYSTEM.BARCODE_SCANNER_TYPE.toString()

    // Retrieve other info via Intent
   private val description: String
        get() {
            val builder = StringBuilder(
                "Scanner Type: " + scannerType + "\n"
                        + "Boot Type: " + SYSTEM.BOOT_TYPE.toString() + "\n"
                        + "Device Model: " + android.os.Build.MODEL + "\n"
                        + "WiFi type: " + SYSTEM.WIFI_TYPE.toString() + "\n"
                        + "Firmware Version: " + SYSTEM.getVersions()["FIRMWARE"] + "\n"
                        + "Kernel Version: " + SYSTEM.getVersions()["KERNEL"] + "\n"
            )
            val ifilter = IntentFilter(SYSTEM.Version.ACTION_DEVICE_INFO)
            val info = this.registerReceiver(null, ifilter)
            val b = info!!.extras
            val bundleStrings = b!!.keySet()
            var tmp: Any?

            for (st in bundleStrings) {
                builder.append("$st: ")
                tmp = b.get(st)
                if (tmp != null && tmp is Int) {
                    builder.append(tmp as Int?)
                } else if (tmp != null && tmp is String) {
                    builder.append(tmp as String?)
                } else {
                    continue
                }
                builder.append("\n")
            }
            return builder.toString()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        txtInfo = findViewById<TextView>(R.id.txtInfo)
        btnBg = findViewById<ImageButton>(R.id.btnBg)

        getInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.reset, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_reset) {
            getInfo()
            btnBg!!.setImageResource(R.drawable.ic_launcher)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Shows device information.
     */
    private fun getInfo() {
        txtInfo!!.text = description
    }

    /**
     * Activated by btnBg.  Displays DEVICE_IMAGE in btnBg.
     */
    fun btnBgOnClick(v: View) {
        val img = SYSTEM.DEVICE_IMAGE
        val ratio = img.height.toFloat() / img.width.toFloat()
        val width = 200
        val height = ceil((width * ratio).toDouble()).toInt()
        btnBg!!.setImageBitmap(
            Bitmap.createScaledBitmap(img, width, height, true)
        )
    }
}
