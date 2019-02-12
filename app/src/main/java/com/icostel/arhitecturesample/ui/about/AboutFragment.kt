package com.icostel.arhitecturesample.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.ui.BaseFragment

class AboutFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_about, container, false)
    }
}