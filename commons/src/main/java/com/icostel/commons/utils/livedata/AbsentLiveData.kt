package com.icostel.commons.utils.livedata

import androidx.lifecycle.LiveData

/**
 * A LiveData class that has `null` value.
 */
class AbsentLiveData<T : Any?> private constructor() : LiveData<T>() {
    init {
        // use post instead of set since this can be created on any thread
        postValue(null)
    }

    companion object {
        @JvmStatic
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}
