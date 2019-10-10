package com.icostel.arhitecturesample.di

import android.app.Activity
import android.app.Application
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
        //TODO create an impl for this to reduce the boiler plate shown here
        sampleApp.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                handleActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {

            }

            override fun onActivityDestroyed(activity: Activity) {

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
