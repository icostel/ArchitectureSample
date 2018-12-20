package com.icostel.arhitecturesample.utils.error

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorData(
        val tag: String?,
        val message: String?,
        val button: String?,
        val autoDismiss: Boolean = true,
        var userAction: ErrorHandler.UserAction? = null,
        val errorType: ErrorType = ErrorType.Error
) : Parcelable

enum class ErrorType {
    Error, Success
}
