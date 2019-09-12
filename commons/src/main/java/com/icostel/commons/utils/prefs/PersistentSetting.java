package com.icostel.commons.utils.prefs;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import timber.log.Timber;

public abstract class PersistentSetting<T> implements Setting<T> {
    private SharedPreferences preferences;
    private PreferenceLiveData data;
    private Gson gson;

    public PersistentSetting(SharedPreferences preferences) {
        this.preferences = preferences;
        gson = new Gson();

        data = new PreferenceLiveData();
        data.postValue(read());
    }

    @Override
    public LiveData<T> value() {
        return data;
    }

    @Override
    public T get() {
        return data.getValue();
    }

    @Override
    public T instantValue() {
        T instantValue = read();
        data.postValue(instantValue);
        return instantValue;
    }

    @Override
    public void updateValue(T value) {
        write(value);
        data.postValue(value);
    }

    @Override
    public boolean hasValue() {
        return data.getValue() != null;
    }

    @SuppressWarnings("unchecked")
    private T read() {
        try {
            Class<T> clazz = clazz();
            if (clazz == String.class) {
                return (T) preferences.getString(key(), null);
            } else if (clazz == Boolean.class) {
                return (T) Boolean.valueOf(preferences.getString(key(), null));
            } else if (clazz == Integer.class) {
                return (T) Integer.valueOf(preferences.getString(key(), null));
            } else {
                // Fallback to Gson
                return gson.fromJson(preferences.getString(key(), null), clazz);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    private void write(T data) {
        Class<T> clazz = clazz();
        String encoded = null;
        if (data != null) {
            if (clazz == String.class) {
                encoded = (String) data;
            } else if (clazz == Boolean.class) {
                encoded = String.valueOf(data);
            } else if (clazz == Integer.class) {
                encoded = String.valueOf(data);
            } else {
                // Fallback to Gson
                encoded = gson.toJson(data);
            }
        }
        Timber.d("write() " + encoded + " with key: " + key());
        preferences.edit().putString(key(), encoded).apply();
        this.data.postValue(data);
    }

    private void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.equals(key, key())) {
            data.setValue(read());
        }
    }

    protected abstract String key();

    protected abstract Class<T> clazz();

    private class PreferenceLiveData extends MutableLiveData<T> {
        @Override
        protected void onActive() {
            super.onActive();
            preferences.registerOnSharedPreferenceChangeListener(PersistentSetting.this::onSharedPreferenceChanged);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            preferences.unregisterOnSharedPreferenceChangeListener(PersistentSetting.this::onSharedPreferenceChanged);
        }
    }
}
