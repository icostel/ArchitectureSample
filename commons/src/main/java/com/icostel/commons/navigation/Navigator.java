package com.icostel.commons.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;


public interface Navigator {
    void navigateTo(@Nullable NavigationAction navigationAction);

    default @IdRes
    int getFragmentContainer() {
        return 0;
    }

    FragmentManager getSupportFragmentManager();

    void startActivity(Intent intent);

    void startActivity(Intent intent, Bundle options);

    void startActivityForResult(Intent intent, int requestCode);

    void finish();

    Context getContext();

    void setResult(int resultCode, Intent data);

    PackageManager getPackageManager();
}
