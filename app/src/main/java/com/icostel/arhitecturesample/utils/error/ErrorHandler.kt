package com.icostel.arhitecturesample.utils.error

import androidx.annotation.MainThread

interface ErrorHandler {

    enum class UserAction {
        Nothing,
        Button,
        Dismiss
    }

    @MainThread
    fun onUserErrorAction(errorData: ErrorData?)
}
