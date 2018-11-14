package com.icostel.arhitecturesample.utils.error;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.utils.prefs.AnimationEndListener;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ErrorFragment extends Fragment {

    private static final int FADE_ANIMATION_DURATION = 500;
    private static final int FADE_IN_OUT_DELAY = 3000;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ErrorViewModel errorViewModel;

    private TextView errorText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        errorViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ErrorViewModel.class);

        errorText = view.findViewById(R.id.error_tv);

        errorViewModel.getError().observe(this, error -> {
            if (error != null) {
                errorText.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(error.errMsg)) {
                    errorText.setText(error.errMsg);
                }
                fadeIn(errorText);
                if (error.shouldAutoDismiss) {
                    new Handler().postDelayed(() -> fadeOut(errorText), FADE_IN_OUT_DELAY);
                } else {
                    errorText.setOnClickListener(v -> {
                        errorViewModel.onDismissClicked(error);
                        fadeOut(errorText);
                    });
                }
            }
        });
    }

    private void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);

        ObjectAnimator
                .ofFloat(view, "alpha", 0f, 1f)
                .setDuration(FADE_ANIMATION_DURATION)
                .start();
    }

    private void fadeOut(View view) {
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view, "alpha", 1f, 0f)
                .setDuration(FADE_ANIMATION_DURATION);
        animator.addListener((AnimationEndListener) () -> view.setVisibility(View.GONE));
        animator.start();
    }

    public void makeError(@NonNull ErrorData errorData) {
        errorViewModel.makeError(errorData);
    }

    public void hideError() {
        fadeOut(errorText);
    }
}