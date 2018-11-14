package com.icostel.arhitecturesample.utils.error

import androidx.annotation.MainThread

interface ErrorHandler {
    var errorFragment: ErrorFragment?

    enum class UserAction {
        Nothing,
        Dismiss
    }

    @MainThread
    fun onUserErrorAction(errorData: ErrorData?)
}
