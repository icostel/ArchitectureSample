package com.icostel.commons.utils

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppExecutors @Inject
internal constructor() {

    private val diskIO: Executor
    private val networkIO: Executor
    private val delayedNetworkIO: Executor
    private val mainThread: Executor
    private val background: Executor

    init {
        diskIO = Executors.newSingleThreadExecutor()
        networkIO = Executors.newFixedThreadPool(NETWORK_THREADS)
        background = Executors.newSingleThreadExecutor()
        delayedNetworkIO = NetworkIODelayedExecutor()
        mainThread = MainThreadExecutor()
    }

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun delayedNetworkIO(): Executor {
        return delayedNetworkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    fun background(): Executor {
        return background
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(runnable: Runnable) {
            mainThreadHandler.post(runnable)
        }
    }

    private class NetworkIODelayedExecutor : Executor {
        private val mainThreadHandler = Handler()

        override fun execute(runnable: Runnable) {
            mainThreadHandler.postDelayed(runnable, NETWORK_DELAY_MILLIS)
        }
    }

    companion object {
        private const val NETWORK_THREADS = 3
        private const val NETWORK_DELAY_MILLIS = 3000L
    }
}

