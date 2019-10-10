package com.icostel.arhitecturesample.di.factory

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters

interface BaseWorkerFactory {
    fun createWorker(appContext: Context, params: WorkerParameters): RxWorker
}