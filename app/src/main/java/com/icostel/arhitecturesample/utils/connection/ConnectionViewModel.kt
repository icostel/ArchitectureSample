package com.icostel.arhitecturesample.utils.connection

import android.app.Application
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ConnectionViewModel @Inject constructor(context: Application) : ViewModel() {
    var connectionLiveData = ConnectionLiveData(context)

    fun get() : ConnectionLiveData {
        return this.connectionLiveData
    }
}