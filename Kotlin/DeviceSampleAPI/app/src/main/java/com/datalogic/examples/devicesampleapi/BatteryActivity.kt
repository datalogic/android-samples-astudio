// ©2019 Datalogic S.p.A. and/or its affiliates. All rights reserved.

package com.datalogic.examples.devicesampleapi

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.datalogic.device.battery.BatteryInfo
import com.datalogic.device.battery.DLBatteryManager

/**
 * Activity for battery information.
 */
class BatteryActivity : Activity() {

    private var txtPower: TextView? = null
    private var batteryStatus: Intent? = null

    private val batteryInfo: String
        get() {
            val deviceBattery = DLBatteryManager.getInstance()
            return ("  capacity: " + deviceBattery.getIntProperty(BatteryInfo.CAPACITY_REMAINING) + "\n"
                    + "  year: " + deviceBattery.getIntProperty(BatteryInfo.PRODUCTION_YEAR) + "\n"
                    + "  week: " + deviceBattery.getIntProperty(BatteryInfo.PRODUCTION_WEEK) + "\n"
                    + "  serial_number: " + deviceBattery.getStringProperty(BatteryInfo.SERIAL_NUMBER) + "\n"
                    + "  manufacturer: " + deviceBattery.getStringProperty(BatteryInfo.MANUFACTURER) + "\n")
        }

    private val extPowerStatus: Boolean
        get() {
            val external_power_source = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            return external_power_source == BatteryManager.BATTERY_PLUGGED_AC
        }

   private val usbPowerStatus: Boolean
        get() {
            val chargePlug = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            return chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        }

   private val status: String
        get() {
            val resultStatus: String
            val status = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

            when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> resultStatus = "Charging"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> resultStatus = "Discharging"
                BatteryManager.BATTERY_STATUS_FULL -> resultStatus = "Full"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> resultStatus = "Not charging"
                else -> resultStatus = "Unknown"
            }

            return resultStatus
        }

    val chargingStatus: Boolean
        get() {
            val status = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            return status == BatteryManager.BATTERY_STATUS_CHARGING
        }

    val dischargingStatus: Boolean
        get() {
            val status = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            return status == BatteryManager.BATTERY_STATUS_DISCHARGING
        }

    private val currentLevel: Float
        get() {
            val level = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            return level / scale.toFloat()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)

        txtPower = findViewById(R.id.txtPower) as TextView

        // Get Android's status.
        batteryStatus = registerReceiver(
            null, IntentFilter(
                Intent.ACTION_BATTERY_CHANGED
            )
        )

        // Change displayed text.
        setText()
    }

    // updates showed TextView with battery infos.
    private fun setText() {
        txtPower!!.text = ""
        txtPower!!.text = ("Battery Info: \n" + batteryInfo + "\n"
                + "Battery Status: " + status + "\n"
                + "External AC Power: " + extPowerStatus + "\n"
                + "External USB Power: " + usbPowerStatus + "\n"
                + "Current level: " + currentLevel + "\n")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.reset, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reset -> setText()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}
