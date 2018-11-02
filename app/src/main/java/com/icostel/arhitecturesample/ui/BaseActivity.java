package com.icostel.arhitecturesample.ui;

import android.content.Context;
import android.view.MenuItem;

import com.icostel.arhitecturesample.di.InjectableActivity;
import com.icostel.arhitecturesample.navigation.NavigationAction;
import com.icostel.arhitecturesample.navigation.Navigator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import timber.log.Timber;

public abstract class BaseActivity extends InjectableActivity implements Navigator {

    @Override
    public void navigateTo(@Nullable NavigationAction navigationAction) {
        if (navigationAction != null) {
            navigationAction.navigate(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected final void enableUpNavigation() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Timber.w("no action bar available");
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
