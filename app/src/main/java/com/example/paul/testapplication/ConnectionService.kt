package com.example.paul.testapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.support.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.produce


class ConnectionService(context: Context) {

    private val stateChannel: BroadcastChannel<NetworkState> by lazy {
        return@lazy ConflatedBroadcastChannel<NetworkState>()
    }

    private fun sendValue(state: NetworkState) {
        CoroutineScope(Dispatchers.Main).produce<NetworkState> {
            stateChannel.send(state)
        }
    }

    private var networkState = NetworkState.OFFLINE
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network?) {
            sendValue(NetworkState.ONLINE)
        }

        override fun onLost(network: Network?) {
            sendValue(NetworkState.OFFLINE)
        }

        override fun onUnavailable() {
            sendValue(NetworkState.OFFLINE)
        }
    }

    fun updateState() {
        if (connectivityManager != null) {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork?.run {
                when {
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT -> {
                        networkState = if (isConnectedOrConnecting) NetworkState.ONLINE else NetworkState.OFFLINE
                        sendValue(networkState)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            connectivityManager.registerDefaultNetworkCallback(networkCallback)
                        } else {
                            val networkRequest = NetworkRequest.Builder().build()
                            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
                        }
                    }
                    else -> {
                        sendValue(NetworkState.OFFLINE)
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getNetworkState(): BroadcastChannel<NetworkState> {
        return stateChannel
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