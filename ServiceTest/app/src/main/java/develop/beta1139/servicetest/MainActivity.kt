package develop.beta1139.servicetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import develop.beta1139.servicetest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }
    private var localBinder: LocalService.LocalBinder? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e("dbg", "onServiceConnected")
            localBinder = service as LocalService.LocalBinder
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("dbg", "onServiceDisconnected")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        if (localBinder == null) {
            val intent = Intent(applicationContext, LocalService::class.java)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        val intent = Intent(applicationContext, LocalService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}