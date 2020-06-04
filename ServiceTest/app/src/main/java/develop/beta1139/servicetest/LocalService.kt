package develop.beta1139.servicetest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat

class LocalService : Service() {
    private val binder = LocalBinder()
    private var notificationManager: NotificationManager? = null
    private val channelId = "LocalServiceTest"
    private var handler: Handler? = null

    inner class LocalBinder : Binder() {
        fun getService(): LocalService = this@LocalService
    }

    override fun onCreate() {
        Log.e("dbg", "onCreate")
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setContentTitle("")
            .setContentText("").build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("dbg", "onStartCommand")

        if (handler == null) {
            handler = Handler()
            var count = 0
            val r = object : Runnable {
                override fun run() {
                    Toast.makeText(this@LocalService, "count: $count", Toast.LENGTH_SHORT).show()
                    count++
                    handler?.postDelayed(this, 2000)
                }
            }
            handler?.post(r)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.e("dbg", "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("dbg", "onUnbind")
        return super.onUnbind(intent)

    }

    override fun onDestroy() {
        Log.e("dbg", "onDestroy")
        super.onDestroy()
    }
}