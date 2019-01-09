package com.icostel.arhitecturesample.utils;

import android.animation.Animator;

public interface AnimationEndListener extends Animator.AnimatorListener {
    @Override
    default void onAnimationStart(Animator animation) {
    }

    @Override
    default void onAnimationEnd(Animator animation) {
        onAnimationDone();
    }

    @Override
    default void onAnimationCancel(Animator animation) {
    }

    @Override
    default void onAnimationRepeat(Animator animation) {
    }

    void onAnimationDone();

}
