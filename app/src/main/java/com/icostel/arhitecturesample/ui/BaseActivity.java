package com.icostel.arhitecturesample.ui;

import android.content.Context;

import com.icostel.arhitecturesample.di.InjectableActivity;
import com.icostel.arhitecturesample.navigation.NavigationAction;
import com.icostel.arhitecturesample.navigation.Navigator;

import androidx.annotation.Nullable;

public abstract class BaseActivity extends InjectableActivity implements Navigator {

    @Override
    public void navigateTo(@Nullable NavigationAction navigationAction) {
        if (navigationAction != null) {
            navigationAction.navigate(this);
        }
    }

    // overwrite this if you need to navigate using fragments, use the container ID
    @Override
    public int getFragmentContainer() {
        return 0;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
