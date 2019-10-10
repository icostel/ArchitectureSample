package com.icostel.arhitecturesample.work

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters

interface BaseWorkerFactory {
    fun createWorker(appContext: Context, params: WorkerParameters): RxWorker
}