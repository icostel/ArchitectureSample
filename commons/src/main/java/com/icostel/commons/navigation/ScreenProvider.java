package com.icostel.commons.navigation;

import android.util.SparseArray;

public abstract class ScreenProvider {

    private static final Object LOCK = new Object() {};
    static final int FINISH = 0;

    protected static final SparseArray<Class> SCREEN_MAP = new SparseArray<>();

    public ScreenProvider() {
        SCREEN_MAP.put(FINISH, null);
    }

    Class getScreen(int screen) {
        synchronized (LOCK) {
            return SCREEN_MAP.get(screen);
        }
    }

    //reminder to add the specific screens
    protected abstract void initScreenMap();
}
