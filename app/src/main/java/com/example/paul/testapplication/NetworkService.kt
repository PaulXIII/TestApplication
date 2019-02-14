package com.example.paul.testapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.support.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce


class NetworkService(private val context: Context) {

    private var networkState = NetworkState.OFFLINE
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network?) {
            networkState = NetworkState.ONLINE
        }

        override fun onLost(network: Network?) {
            networkState = NetworkState.OFFLINE
        }

        override fun onUnavailable() {
            super.onUnavailable()
            networkState = NetworkState.OFFLINE
        }
    }

    fun updateState() {
        if (connectivityManager != null) {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork?.run {
                when {
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT -> {
                        networkState = if (isConnectedOrConnecting) NetworkState.ONLINE else NetworkState.OFFLINE
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                        val networkRequest = NetworkRequest.Builder().build()
                        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                        connectivityManager.registerDefaultNetworkCallback(networkCallback)
                    }
                    else -> {
                        networkState = NetworkState.OFFLINE
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getNetworkState(): ReceiveChannel<NetworkState> {
        return GlobalScope.produce {
            send(networkState)
        }
    }

    fun unregisterCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                connectivityManager?.unregisterNetworkCallback(networkCallback)
            } catch (e: Exception) {
            }
        }
    }

    enum class NetworkState {
        ONLINE, OFFLINE
    }
}