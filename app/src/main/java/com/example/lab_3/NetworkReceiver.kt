package com.example.lab_3

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent?) {
        println("Network Receiver onReceive")

        val connectionManager = getSystemService(p0, ConnectivityManager::class.java)
        val networkInfo = connectionManager?.activeNetworkInfo;
        println("Is Connected: ${networkInfo?.isConnected}")
        println("Is Connected: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI}")
    }
}
