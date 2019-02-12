package com.icostel.commons.utils.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.icostel.commons.utils.AppExecutors
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

// extensions that manage concatenation of live data (the source will emit values when at least one provided live data has changed)

@Singleton
class ConcatUtils
@Inject constructor(private val executors: AppExecutors) {

    fun <X, Y, R> concat2(first: LiveData<X>,
                         second: LiveData<Y>,
                         concatFct: (X?, Y?) -> R?,
                         executor: Executor? = executors.diskIO()): LiveData<R> = MediatorLiveData<R>().apply {

        var firstValue: X? = null
        var secondValue: Y? = null

        var firstChanged = false
        var secondChanged = false

        fun updateValueIfNeeded() {
            if (firstChanged && secondChanged) {
                executor?.let {
                    postValue(concatFct(firstValue, secondValue))
                } ?: executors.diskIO().execute {
                    postValue(concatFct(firstValue, secondValue))
                }
            }
        }

        addSource(first) {
            firstChanged = true
            firstValue = it
            updateValueIfNeeded()
        }

        addSource(second) {
            secondChanged = true
            secondValue = it
            updateValueIfNeeded()
        }
    }

    fun <X, Y, Z, R> concat3(first: LiveData<X>,
                            second: LiveData<Y>,
                            third: LiveData<Z>,
                            concatFct: (X?, Y?, Z?) -> R?,
                            executor: Executor? = executors.diskIO()): LiveData<R> = MediatorLiveData<R>().apply {

        var firstValue: X? = null
        var secondValue: Y? = null
        var thirdValue: Z? = null

        var firstChanged = false
        var secondChanged = false
        var thirdChanged = false

        fun updateValueIfNeeded() {
            if (firstChanged && secondChanged && thirdChanged) {
                executor?.let {
                    postValue(concatFct(firstValue, secondValue, thirdValue))
                } ?: executors.diskIO().execute {
                    postValue(concatFct(firstValue, secondValue, thirdValue))
                }
            }
        }

        addSource(first) {
            firstChanged = true
            firstValue = it
            updateValueIfNeeded()
        }

        addSource(second) {
            secondChanged = true
            secondValue = it
            updateValueIfNeeded()
        }

        addSource(third) {
            thirdChanged = true
            thirdValue = it
            updateValueIfNeeded()
        }
    }

    fun <X, Y, Z, T, R> concat4(first: LiveData<X>,
                               second: LiveData<Y>,
                               third: LiveData<Z>,
                               fourth: LiveData<T>,
                               concatFct: (X?, Y?, Z?, T?) -> R?,
                               executor: Executor? = executors.diskIO()): LiveData<R> = MediatorLiveData<R>().apply {

        var firstValue: X? = null
        var secondValue: Y? = null
        var thirdValue: Z? = null
        var fourthValue: T? = null

        var firstChanged = false
        var secondChanged = false
        var thirdChanged = false
        var fourthChanged = false

        fun updateValueIfNeeded() {
            if (firstChanged && secondChanged && thirdChanged && fourthChanged) {
                executor?.let {
                    postValue(concatFct(firstValue, secondValue, thirdValue, fourthValue))
                } ?: executors.diskIO().execute {
                    postValue(concatFct(firstValue, secondValue, thirdValue, fourthValue))
                }
            }
        }

        addSource(first) {
            firstChanged = true
            firstValue = it
            updateValueIfNeeded()
        }

        addSource(second) {
            secondChanged = true
            secondValue = it
            updateValueIfNeeded()
        }

        addSource(third) {
            thirdChanged = true
            thirdValue = it
            updateValueIfNeeded()
        }

        addSource(fourth) {
            fourthChanged = true
            fourthValue = it
            updateValueIfNeeded()
        }
    }

}