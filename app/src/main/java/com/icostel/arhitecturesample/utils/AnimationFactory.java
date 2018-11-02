package com.icostel.arhitecturesample.utils;

import android.animation.ObjectAnimator;
import android.view.View;

import com.bumptech.glide.request.transition.ViewPropertyTransition;

/**
 * Defines general object transitions
 */
public final class AnimationFactory {

    private AnimationFactory() {/* does not instantiate */}

    private static final int ANIMATION_DURATION = 2000;
    private static final String ANIMATION_PROPERTY_ALPHA = "alpha";

    public enum AnimationType {
        FADE_IN,
        FADE_OUT,
        DEFAULT
    }

    public static ViewPropertyTransition.Animator getTransition(AnimationType animationType) {
        switch (animationType) {
            case FADE_IN:
                return FADE_IN_TRANSITION;
            case FADE_OUT:
                return FADE_OUT_TRANSITION;
            default:
                return DEFAULT_TRANSITION;
        }
    }

    private static final ViewPropertyTransition.Animator FADE_IN_TRANSITION = v -> fadeInAnimator(v).start();
    private static final ViewPropertyTransition.Animator FADE_OUT_TRANSITION = v -> fadeOutAnimator(v).start();
    private static final ViewPropertyTransition.Animator DEFAULT_TRANSITION = v -> defaultAnimator(v).start();


    private static ObjectAnimator fadeInAnimator(View v) {
        v.setAlpha(0f);
        ObjectAnimator fadeInAnim = ObjectAnimator.ofFloat(v, ANIMATION_PROPERTY_ALPHA, 0f, 1f);
        fadeInAnim.setDuration(ANIMATION_DURATION);
        return fadeInAnim;
    }

    private static ObjectAnimator fadeOutAnimator(View v) {
        v.setAlpha(1f);
        ObjectAnimator fadeOutAnim = ObjectAnimator.ofFloat(v, ANIMATION_PROPERTY_ALPHA, 1f, 0f);
        fadeOutAnim.setDuration(ANIMATION_DURATION);
        return fadeOutAnim;
    }

    //does nothing
    private static ObjectAnimator defaultAnimator(View v) {
        ObjectAnimator defaultAnim = ObjectAnimator.ofFloat(v, ANIMATION_PROPERTY_ALPHA, 1f, 1f);
        defaultAnim.setDuration(0);
        return defaultAnim;
    }

}
