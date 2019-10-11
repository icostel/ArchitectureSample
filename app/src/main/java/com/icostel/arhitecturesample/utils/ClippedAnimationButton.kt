package com.icostel.arhitecturesample.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.RelativeLayout
import pXToDp

class ClippedAnimationButton: RelativeLayout {
    private var foregroundBtn: Button
    private var backgroundBtn: Button

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        foregroundBtn = Button(context)
        backgroundBtn = Button(context)

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, WRAP_CONTENT)
        params.addRule(CENTER_IN_PARENT, TRUE)
        params.setMargins(pXToDp(context, 32), 0, pXToDp(context, 32), 0)
        params.width = MATCH_PARENT
        params.height = WRAP_CONTENT
        addView(foregroundBtn, params)
        addView(backgroundBtn, params)
    }
}