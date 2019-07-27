package com.datalogic.examples.decodeintent


import android.app.NotificationManager as NotificationManager
import android.app.Service
import android.content.Intent
import android.content.Context
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat


/**
 * Log and Toast the received action, then display a notification containing result.
 */
class IntentService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        showMessage("Started IntentService, check notifications")
        Log.d(javaClass.name, "Started service with Intent")

        val categoryAll = intent.categories
        val category = StringBuilder()
        for (s in categoryAll) {
            category.append(s)
        }

        val type = intent.getStringExtra(IntentWedgeSample.EXTRA_TYPE)
        // Retrieve result data.
        var data = intent.getStringExtra(IntentWedgeSample.EXTRA_DATA_STRING)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // We are going to show a notification, don't exaggerate with characters.
        if (data!!.length > 20) {
            data = data.substring(0, 20)
        }

        // From Android API 26+ , use NotificationChannel
        val channelId = "0"
        val channelName = "Intent Service"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val aChannel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(aChannel)

        val mBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(type)
            .setContentText("Result: $data")

        // Show it.
        notificationManager.notify(0, mBuilder.build())

        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
