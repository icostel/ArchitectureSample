package com.icostel.commons.navigation;

public abstract class NavigationAction {
    Integer requestCode;
    boolean shouldFinish;

    public abstract void navigate(Navigator navigator);

    void finishIfNeeded(Navigator navigator) {
        if (shouldFinish) {
            navigator.finish();
        }
    }
}
