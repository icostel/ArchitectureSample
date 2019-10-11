package com.icostel.arhitecturesample.ui.screens

import androidx.fragment.app.Fragment

abstract class BackFragment : Fragment() {
    var shouldHandleBack = false

    open fun onBackPress() { /* default nothing */
    }
}
