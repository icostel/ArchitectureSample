package com.icostel.commons.utils

import android.animation.ObjectAnimator
import android.view.View

/**
 * Defines general object transitions
 */
class AnimatorFactory private constructor() {

    enum class AnimationType {
        FADE_IN,
        FADE_OUT,
        DEFAULT
    }

    companion object {

        private const val ANIMATION_DURATION = 2000
        private const val ANIMATION_PROPERTY_ALPHA = "alpha"

        fun getAnimator(animationType: AnimationType, v: View?): ObjectAnimator? {
            return if (v != null) {
                when (animationType) {
                    AnimationType.FADE_IN -> fadeInAnimator(v)
                    AnimationType.FADE_OUT -> fadeOutAnimator(v)
                    AnimationType.DEFAULT -> defaultAnimator(v)
                }
            } else {
                null
            }
        }

        private fun fadeInAnimator(v: View): ObjectAnimator {
            v.alpha = 0f
            val fadeInAnim = ObjectAnimator.ofFloat(v, ANIMATION_PROPERTY_ALPHA, 0f, 1f)
            fadeInAnim.duration = ANIMATION_DURATION.toLong()
            return fadeInAnim
        }

        private fun fadeOutAnimator(v: View): ObjectAnimator {
            v.alpha = 1f
            val fadeOutAnim = ObjectAnimator.ofFloat(v, ANIMATION_PROPERTY_ALPHA, 1f, 0f)
            fadeOutAnim.duration = ANIMATION_DURATION.toLong()
            return fadeOutAnim
        }

        //does nothing
        private fun defaultAnimator(v: View): ObjectAnimator {
            val defaultAnim = ObjectAnimator.ofFloat(v, ANIMATION_PROPERTY_ALPHA, 1f, 1f)
            defaultAnim.duration = 0
            return defaultAnim
        }

    }
}
