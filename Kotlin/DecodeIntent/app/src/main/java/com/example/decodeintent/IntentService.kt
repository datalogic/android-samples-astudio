package com.example.decodeintent


import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast


/**
 * Log and Toast the received action, then display a notification containing result.
 */
class IntentService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        showMessage("Started IntentService, check notifications")
        Log.d(javaClass.name, "Started service with Intent")

        val category_all = intent.categories
        val category = StringBuilder()
        for (s in category_all) {
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

        val mBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(type)
            .setContentText("Result: $data")

        // Show it.
        notificationManager.notify(0, mBuilder.build())

        stopSelf()
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
