package com.icostel.arhitecturesample.di.keys

import androidx.work.RxWorker
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out RxWorker>)