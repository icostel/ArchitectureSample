package com.icostel.arhitecturesample.ui

import androidx.fragment.app.Fragment

abstract class BackFragment : Fragment() {
    private var shouldHandleBack = false

    fun setShouldHandleBack(shouldHandleBack: Boolean) {
        this.shouldHandleBack = shouldHandleBack
    }

    fun shouldHandleBack(): Boolean {
        return shouldHandleBack
    }

    open fun onBackPress() { /* default nothing */
    }
}
