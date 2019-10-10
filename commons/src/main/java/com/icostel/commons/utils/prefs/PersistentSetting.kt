package com.icostel.commons.utils.prefs

import android.content.SharedPreferences
import android.text.TextUtils

import com.google.gson.Gson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

abstract class PersistentSetting<T>(
        private val preferences: SharedPreferences,
        private val gSon: Gson = Gson()
) : Setting<T> {

    private val data: PreferenceLiveData = PreferenceLiveData()

    //register to data events from here
    override fun getData(): LiveData<T?> {
        return data
    }

    override // gets the latest getData stored and pushed a new update of the data
    fun getValue(): T? {
        val instantValue = read()
        data.postValue(instantValue)
        return instantValue
    }

    override fun updateValue(value: T?) {
        write(value)
        data.postValue(value)
    }

    override fun hasValue(): Boolean {
        return data.value != null
    }

    @Suppress("UNCHECKED_CAST")
    private fun read(): T? {
        try {
            val clazz = clazz()
            return when(clazz()) {
                String::class.java -> preferences.getString(key(), null) as T?
                Boolean::class.java -> preferences.getString(key(), null)?.toBoolean() as T?
                Int::class.java -> preferences.getString(key(), null)?.toInt() as T?
                else -> {
                    gSon.fromJson(preferences.getString(key(), null), clazz)
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }

    private fun write(data: T?) {
        if (data == null) {
            preferences.edit().remove(key()).apply()
        } else {
            val encoded = when(clazz()) {
                String::class.java -> data as String?
                Boolean::class.java, Int::class.java -> data.toString()
                else -> gSon.toJson(data)
            }
            preferences.edit().putString(key(), encoded).apply()
        }
        this.data.postValue(data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (TextUtils.equals(key, key())) {
            data.value = read()
        }
    }

    protected abstract fun key(): String

    protected abstract fun clazz(): Class<T>

    private inner class PreferenceLiveData : MutableLiveData<T>() {
        override fun onActive() {
            super.onActive()
            preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                this@PersistentSetting.onSharedPreferenceChanged(sharedPreferences, key)
            }
        }

        override fun onInactive() {
            super.onInactive()
            preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                this@PersistentSetting.onSharedPreferenceChanged(sharedPreferences, key)
            }
        }
    }
}
