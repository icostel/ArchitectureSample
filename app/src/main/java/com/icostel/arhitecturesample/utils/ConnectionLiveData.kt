package com.icostel.arhitecturesample.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import com.icostel.arhitecturesample.SampleApp
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Posts updates when we lost all network connections or when we connect back to a network
 *
 * This will emmit false only when all network connections are lost
 * and will remain true as long as we have at least one active connection
 */
@Singleton
class ConnectionLiveData
@Inject constructor(
        val context: SampleApp,
        val connectivityManager: ConnectivityManager
) : MutableLiveData<Boolean>() {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            if(network != null) {
                super.onAvailable(network)
                areWeConnected()
            }
        }

        override fun onLost(network: Network?) {
            if(network != null) {
                super.onLost(network)
                areWeConnected()
            }
        }
    }

    fun areWeConnected() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnectedOrConnecting == true)
    }

    override fun onActive() {
        super.onActive()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        areWeConnected()
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}