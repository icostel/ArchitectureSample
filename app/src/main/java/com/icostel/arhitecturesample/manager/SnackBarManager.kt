package com.icostel.arhitecturesample.manager

import android.app.Activity

interface SnackBarManager {
    fun handleMsg(activity: Activity, msg: String)
}