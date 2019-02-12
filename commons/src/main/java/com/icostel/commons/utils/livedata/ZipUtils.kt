package com.icostel.commons.utils.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.icostel.commons.utils.AppExecutors
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

// extensions that manage zipping live data (the source will emit values once all provided live data have changed)

@Singleton
class ZipUtils
@Inject constructor(private val appExecutors: AppExecutors) {

    fun <X, Y, R> zip2(first: LiveData<X>,
                       second: LiveData<Y>,
                       executor: Executor? = appExecutors.diskIO(),
                       zipFct: (X?, Y?) -> R?): LiveData<R> = MediatorLiveData<R>().apply {

        var firstChanged = false
        var secondChanged = false
        var previousFirst: X? = null
        var previousSecond: Y? = null

        fun updateValueIfNeeded() {
            // do the zip function only if both sources have been changed/updated
            if (firstChanged && secondChanged) {
                firstChanged = false
                secondChanged = false
                executor?.let {
                    postValue(zipFct(previousFirst, previousSecond))
                } ?: appExecutors.diskIO().execute {
                    postValue(zipFct(previousFirst, previousSecond))
                }
            }
        }

        addSource(first) {
            firstChanged = true
            previousFirst = it
            updateValueIfNeeded()
        }

        addSource(second) {
            secondChanged = true
            previousSecond = it
            updateValueIfNeeded()
        }
    }

    fun <X, Y, Z, R> zip3(first: LiveData<X>,
                          second: LiveData<Y>,
                          third: LiveData<Z>,
                          executor: Executor? = appExecutors.diskIO(),
                          zipFct: (X?, Y?, Z?) -> R?): LiveData<R> = MediatorLiveData<R>().apply {

        var firstChanged = false
        var secondChanged = false
        var thirdChanged = false
        var previousFirst: X? = null
        var previousSecond: Y? = null
        var previousThird: Z? = null

        fun updateValueIfNeeded() {

            if (firstChanged && secondChanged && thirdChanged) {
                executor?.let {
                    postValue(zipFct(previousFirst, previousSecond, previousThird))
                } ?: appExecutors.diskIO().execute {
                    postValue(zipFct(previousFirst, previousSecond, previousThird))
                }
                firstChanged = false
                secondChanged = false
                thirdChanged = false
            }
        }

        addSource(first) {
            firstChanged = true
            previousFirst = it
            updateValueIfNeeded()
        }

        addSource(second) {
            secondChanged = true
            previousSecond = it
            updateValueIfNeeded()
        }

        addSource(third) {
            thirdChanged = true
            previousThird = it
            updateValueIfNeeded()
        }
    }

    fun <X, Y, Z, T, R> zip4(first: LiveData<X>,
                             second: LiveData<Y>,
                             third: LiveData<Z>,
                             forth: LiveData<T>,
                             executor: Executor? = appExecutors.diskIO(),
                             zipFct: (X?, Y?, Z?, T?) -> R?): LiveData<R> = MediatorLiveData<R>().apply {

        var firstChanged = false
        var secondChanged = false
        var thirdChanged = false
        var forthChanged = false

        var previousFirst: X? = null
        var previousSecond: Y? = null
        var previousThird: Z? = null
        var previousForth: T? = null

        fun updateValueIfNeeded() {

            if (firstChanged && secondChanged && thirdChanged && forthChanged) {
                executor?.let {
                    postValue(zipFct(previousFirst, previousSecond, previousThird, previousForth))
                } ?: appExecutors.diskIO().execute {
                    postValue(zipFct(previousFirst, previousSecond, previousThird, previousForth))
                }
                firstChanged = false
                secondChanged = false
                thirdChanged = false
                forthChanged = false
            }
        }

        addSource(first) {
            firstChanged = true
            previousFirst = it
            updateValueIfNeeded()
        }

        addSource(second) {
            secondChanged = true
            previousSecond = it
            updateValueIfNeeded()
        }

        addSource(third) {
            thirdChanged = true
            previousThird = it
            updateValueIfNeeded()
        }

        addSource(forth) {
            forthChanged = true
            previousForth = it
            updateValueIfNeeded()
        }
    }
}
