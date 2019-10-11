package com.icostel.arhitecturesample.ui.screens

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment

class LoadingView : RelativeLayout {
    private val progressBar: ProgressBar

    var isLoading: Boolean = false
        set(value) {
            field = value
            this.alpha = if (value) 1.0f else 0.0f
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

        fun attach(activity: Activity): LoadingView {
            val loadingView = LoadingView(activity)
            loadingView.translationZ = 20f
            loadingView.setBackgroundColor(activity.resources.getColor(android.R.color.white, activity.theme))
            activity.findViewById<ViewGroup>(android.R.id.content).addView(loadingView)
            loadingView.isLoading = false
            return loadingView
        }

        fun attach(fragment: Fragment): LoadingView? {
            return if (fragment.activity != null) attach(fragment.activity!!) else null
        }
    }
}