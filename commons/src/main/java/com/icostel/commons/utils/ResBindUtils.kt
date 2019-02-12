package com.icostel.commons.utils

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

fun <T : View> Activity.bind(@IdRes idRes: Int): T {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }.value
}

fun <T : View> View.bind(@IdRes idRes: Int): T {
    return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }.value
}
