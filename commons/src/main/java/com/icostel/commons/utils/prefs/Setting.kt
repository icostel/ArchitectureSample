package com.icostel.commons.utils.prefs

import androidx.lifecycle.LiveData

interface Setting<T> {
    fun getData(): LiveData<T?>
    fun getValue(): T?
    fun updateValue(value: T?)
    fun hasValue(): Boolean
}
