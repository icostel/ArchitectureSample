package com.icostel.arhitecturesample.navigation;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;


public interface Navigator {
    void navigateTo(@Nullable NavigationAction navigationAction);

    @IdRes
    int getFragmentContainer();

    FragmentManager getSupportFragmentManager();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    void finish();

    Context getContext();
}
