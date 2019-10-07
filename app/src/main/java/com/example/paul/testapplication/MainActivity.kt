package com.example.paul.testapplication

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.paul.testapplication.screen.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val connection: ConnectionService by lazy {
        return@lazy ConnectionService(this)
    }

    private var connectionReceiver: ConnectionReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connection.updateState()

        if (savedInstanceState == null) {
            val fragment = MainFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver()
    }

    private fun registerReceiver() {
        unregisterReceiver()
        connectionReceiver = ConnectionReceiver(connection)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectionReceiver, intentFilter)
    }

    private fun unregisterReceiver() {
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver)
            connectionReceiver = null
        }
        connection.unregisterCallback()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            connection.getNetworkState().consumeEach {
                Log.d("TAG", "Network state " + it.name)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver()
    }
}
