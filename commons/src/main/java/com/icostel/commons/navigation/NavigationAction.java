package com.icostel.commons.navigation;

public abstract class NavigationAction {
    protected Integer requestCode;
    protected boolean shouldFinish;

    public abstract void navigate(Navigator navigator);

    protected void finishIfNeeded(Navigator navigator) {
        if (shouldFinish) {
            navigator.finish();
        }
    }
}
