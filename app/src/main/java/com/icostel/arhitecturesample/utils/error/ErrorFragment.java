package com.icostel.arhitecturesample.utils.error;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.Injectable;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ErrorFragment extends Fragment implements Injectable {

    private static final int FADE_ANIMATION_DURATION = 500;
    private static final int FADE_IN_OUT_DELAY = 3000;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ErrorViewModel errorViewModel;

    private TextView errorText;
    private TextView errBtn;
    private RelativeLayout rootView;
    private AppCompatImageView errImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        errorViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ErrorViewModel.class);

        errorText = view.findViewById(R.id.error_tv);
        errBtn = view.findViewById(R.id.err_btn);
        rootView = view.findViewById(R.id.root_view);
        errImage = view.findViewById(R.id.err_image);

        errorViewModel.getError().observe(this, error -> {
            if (error != null) {
                customize(error.getErrorType());

                rootView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(error.getMessage())) {
                    errorText.setText(error.getMessage());
                }
                if (!TextUtils.isEmpty(error.getButton())) {
                    errBtn.setVisibility(View.VISIBLE);
                    errBtn.setText(error.getButton());
                    errBtn.setOnClickListener(v -> errorViewModel.onButtonClicked(error));
                } else {
                    errBtn.setVisibility(View.GONE);
                    errBtn.setOnClickListener(v -> errorViewModel.onButtonClicked(error));
                }
                fadeIn(rootView);
                if (error.getAutoDismiss()) {
                    new Handler().postDelayed(() -> fadeOut(rootView), FADE_IN_OUT_DELAY);
                } else {
                    rootView.setOnClickListener(v -> {
                        errorViewModel.onDismissClicked(error);
                        fadeOut(rootView);
                    });
                }
            }
        });

        errorViewModel.isVisible().observe(this, isVisible -> rootView.setVisibility(isVisible ? View.VISIBLE : View.GONE));
    }

    private void customize(ErrorType errorType) {
        // Default is Error, no change there
        switch (errorType) {
            case Success:
                rootView.setBackgroundResource(R.color.success_bg);
                errorText.setTextColor(ContextCompat.getColor(getActivity(), R.color.success));
                errImage.setImageResource(R.drawable.ic_checkmark_success);
                break;
        }
    }

    private void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);

        ObjectAnimator
                .ofFloat(view, View.ALPHA, 0f, 1f)
                .setDuration(FADE_ANIMATION_DURATION)
                .start();
    }

    private void fadeOut(View view) {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view, View.ALPHA, 1f, 0f)
                .setDuration(FADE_ANIMATION_DURATION);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                rootView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator.start();
    }

    public void makeError(@NonNull ErrorData errorData) {
        errorViewModel.makeError(errorData);
    }

    private void hideError() {
        fadeOut(rootView);
    }

    public void hideError(String tag) {
        if (errorViewModel.shouldDismissError(tag)) {
            hideError();
        }
    }
}
