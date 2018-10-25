package com.icostel.arhitecturesample.utils.prefs;

import androidx.lifecycle.LiveData;

public interface Setting<T> {
    LiveData<T> value();
    T get();
    void updateValue(T value);
    boolean hasValue();
    T instantValue();
}
