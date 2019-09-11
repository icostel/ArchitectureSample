package com.icostel.commons

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected var disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}