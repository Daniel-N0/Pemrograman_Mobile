package com.example.apptugas2

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val tvAndroidVersion = findViewById<TextView>(R.id.tvAndroidVersion)
        val tvModel = findViewById<TextView>(R.id.tvModel)
        val tvBrand = findViewById<TextView>(R.id.tvBrand)
        val tvKernel = findViewById<TextView>(R.id.tvKernel)
        val tvBattery = findViewById<TextView>(R.id.tvBattery)
        val tvNetwork = findViewById<TextView>(R.id.tvNetwork)

        tvAndroidVersion.text = "Android Version    : ${Build.VERSION.RELEASE}"
        tvModel.text = "Device Model       : ${Build.MODEL}"
        tvBrand.text = "Manufacturer       : ${Build.BRAND}"
        tvKernel.text = "Kernel Version      : ${System.getProperty("os.version")}"

        val batteryStatus = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        tvBattery.text = "Battery Level        : $level%"

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(network)

        val status = when {
            capabilities == null -> "Offline"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
            else -> "Unknown"
        }

        tvNetwork.text = "Network Status    : $status"

        val scrollView = findViewById<android.view.View>(R.id.scAndroid)
        if (scrollView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}