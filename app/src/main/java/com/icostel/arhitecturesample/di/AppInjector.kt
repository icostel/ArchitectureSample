package com.icostel.arhitecturesample.di

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.icostel.arhitecturesample.SampleApp
import dagger.android.AndroidInjection
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection

object AppInjector {
    fun init(sampleApp: SampleApp) {
        sampleApp.registerActivityLifecycleCallbacks(object: ActivityCreatedCallback {
            override fun doOnCreate(activity: Activity, savedInstanceState: Bundle?) {
                handleActivity(activity)
            }

        })
    }

    // we can't really control the injections of the activity as we have no control over the
    // framework, but we can register listeners and inject at runtime, the same with fragments
    private fun handleActivity(activity: Activity) {
        if (activity is HasAndroidInjector) {
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            if (f is Injectable) {
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }, true)
        }
    }
}
