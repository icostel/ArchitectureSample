package com.icostel.arhitecturesample.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class LoadingView : RelativeLayout {
    private val progressBar: ProgressBar

    var isLoading: Boolean = false
        set(value) {
            field = value
            progressBar.visibility = if (value) View.VISIBLE else View.GONE
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        progressBar = ProgressBar(context)
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_IN_PARENT, TRUE)
        addView(progressBar, params)
    }

    companion object {

        fun attach(activity: FragmentActivity): LoadingView {
            val loadingView = LoadingView(activity)
            loadingView.translationZ = 10f
            activity.findViewById<ViewGroup>(android.R.id.content).addView(loadingView)
            return loadingView
        }

        fun attach(fragment: Fragment): LoadingView? {
            return if (fragment.activity != null) attach(fragment.activity!!) else null
        }
    }
}