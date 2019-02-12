package com.icostel.arhitecturesample.ui;

import androidx.fragment.app.Fragment;

public abstract class BackFragment extends Fragment {
    private boolean shouldHandleBack = false;

    public void setShouldHandleBack(boolean shouldHandleBack) {
        this.shouldHandleBack = shouldHandleBack;
    }

    public boolean shouldHandleBack() {
        return shouldHandleBack;
    }

    public void onBackPress() { /* default nothing */ }
}
