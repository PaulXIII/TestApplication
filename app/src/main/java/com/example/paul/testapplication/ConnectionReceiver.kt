package com.example.paul.testapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ConnectionReceiver(private val connectionService: ConnectionService?) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        connectionService?.updateState()
    }
}