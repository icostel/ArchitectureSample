package com.icostel.arhitecturesample.utils.extensions

import androidx.annotation.MainThread
import androidx.arch.core.util.Function
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.Executor

class Transformations {
    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> mapAsync(executor: Executor, source: LiveData<X>, func: (t: X) -> Y): LiveData<Y> {
            val result = MediatorLiveData<Y>()
            result.addSource(source) {
                executor.execute {
                    result.postValue(func(it))
                }
            }
            return result
        }

        @JvmStatic
        fun <X, Y> map(
                source: LiveData<X>,
                mapFunction: Function<X, Y>): LiveData<Y> {
            val result = MediatorLiveData<Y>()
            result.addSource(source) { x -> result.value = mapFunction.apply(x) }
            return result
        }

        @JvmStatic
        fun <X, Y> switchMap(
                source: LiveData<X>,
                switchMapFunction: Function<X, LiveData<Y>>): LiveData<Y> {
            val result = MediatorLiveData<Y>()
            result.addSource(source, object : Observer<X> {
                var mSource: LiveData<Y>? = null

                override fun onChanged(x: X?) {
                    val newLiveData = switchMapFunction.apply(x)
                    if (mSource === newLiveData) {
                        return
                    }
                    if (mSource != null) {
                        result.removeSource(mSource!!)
                    }
                    mSource = newLiveData
                    if (mSource != null) {
                        result.addSource(mSource!!) { y -> result.value = y }
                    }
                }
            })
            return result
        }
    }
}

fun <S : Any> MediatorLiveData<S>.addSource(executor: Executor, source: LiveData<S>, onChanged: (t: S) -> Unit) {
    addSource(source) {
        executor.execute {
            onChanged(it)
        }
    }
}

fun <X : Any, Y : Any> Transformations.Companion.mapAsync(executor: Executor, source: LiveData<X>, func: (t: X) -> Y): LiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(source) {
        executor.execute {
            result.postValue(func(it))
        }
    }
    return result
}

@MainThread
fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (t: T?) -> Unit) {
    observe(owner, Observer<T> { t -> observer(t) })
}
