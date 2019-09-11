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
 * This will emmit false only when all network connections are lost and will remain true as long as we have at least one active connection
 */
//TODO refactor deprecated stuff
@Singleton
class ConnectionLiveData @Inject constructor(val context: SampleApp) : MutableLiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager? = null
    private var areWeConnected: Boolean? = false

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

        fun areWeConnected() {
            val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
            //post updates only if the current status is different from the previous one
            val currentStatus: Boolean = activeNetwork?.isConnected == true
            if (currentStatus != areWeConnected) {
                areWeConnected = currentStatus
                postValue(areWeConnected)
            }
        }
    }

    override fun onActive() {
        super.onActive()

        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        //register for updates
        connectivityManager?.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
        // first network status update
        areWeConnected = connectivityManager?.activeNetworkInfo?.isConnected == true
        postValue(areWeConnected)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }
}