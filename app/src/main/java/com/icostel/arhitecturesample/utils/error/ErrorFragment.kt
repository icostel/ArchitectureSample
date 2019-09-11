package com.icostel.arhitecturesample.utils.error

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.di.Injectable
import com.icostel.arhitecturesample.ui.BaseFragment
import com.icostel.commons.utils.AnimationEndListener
import com.icostel.commons.utils.AnimatorFactory
import com.icostel.commons.utils.extensions.observe
import timber.log.Timber

class ErrorFragment : BaseFragment(), Injectable {

    private lateinit var errorViewModel: ErrorViewModel

    private lateinit var errorText: TextView
    private lateinit var errBtn: TextView
    private lateinit var rootView: RelativeLayout
    private lateinit var errImage: AppCompatImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        errorViewModel = getViewModel(ErrorViewModel::class.java)

        errorText = view.findViewById(R.id.error_tv)
        errBtn = view.findViewById(R.id.err_btn)
        rootView = view.findViewById(R.id.root_view)
        errImage = view.findViewById(R.id.err_image)

        errorViewModel.error.observe(this) { error ->
            if (error != null) {
                customize(error.errorType)

                rootView.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(error.message)) {
                    errorText.text = error.message
                }
                if (!TextUtils.isEmpty(error.button)) {
                    errBtn.visibility = View.VISIBLE
                    errBtn.text = error.button
                    errBtn.setOnClickListener { errorViewModel.onButtonClicked(error) }
                } else {
                    errBtn.visibility = View.GONE
                    errBtn.setOnClickListener { errorViewModel.onButtonClicked(error) }
                }
                fadeIn(rootView)
                if (error.autoDismiss) {
                    Handler().postDelayed({ fadeOut(rootView) }, FADE_IN_OUT_DELAY.toLong())
                } else {
                    rootView.setOnClickListener {
                        errorViewModel.onDismissClicked(error)
                        fadeOut(rootView)
                    }
                }
            }
        }

        errorViewModel.isVisible.observe(this) { visible ->
            if (visible != null) {
                rootView.visibility = if (visible) View.VISIBLE else View.GONE
            }
        }
    }

    private fun customize(errorType: ErrorType) {
        // Default is Error, no change there`
        when (errorType) {
            ErrorType.Success -> {
                rootView.setBackgroundResource(R.color.success_bg)
                errorText.setTextColor(ContextCompat.getColor(activity as Context, R.color.success))
                errImage.setImageResource(R.drawable.ic_checkmark_success)
            }
            else -> {
                Timber.d("$TAG unknonw type: $errorType")
            }
        }
    }

    private fun fadeIn(view: View) {
        view.visibility = View.VISIBLE

        ObjectAnimator
                .ofFloat(view, View.ALPHA, 0f, 1f)
                .setDuration(FADE_ANIMATION_DURATION.toLong())
                .start()
    }

    private fun fadeOut(view: View?) {
        val animator = AnimatorFactory.getAnimator(AnimatorFactory.AnimationType.FADE_OUT, view)
        animator?.addListener(AnimationEndListener { rootView.visibility = View.GONE })
    }

    fun makeError(errorData: ErrorData) {
        errorViewModel.makeError(errorData)
    }

    private fun hideError() {
        fadeOut(rootView)
    }

    fun hideError(tag: String) {
        if (errorViewModel.shouldDismissError(tag)) {
            hideError()
        }
    }

    companion object {

        private const val FADE_ANIMATION_DURATION = 500
        private const val FADE_IN_OUT_DELAY = 3000
        private const val TAG = "ErrorFragment"
    }
}
