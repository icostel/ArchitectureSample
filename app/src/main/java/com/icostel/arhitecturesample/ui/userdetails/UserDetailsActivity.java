package com.icostel.arhitecturesample.ui.userdetails;

import android.os.Bundle;
import android.text.TextUtils;

import com.bumptech.glide.GenericTransitionOptions;
import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.di.modules.GlideApp;
import com.icostel.arhitecturesample.ui.BaseActivity;
import com.icostel.arhitecturesample.utils.AnimationFactory;
import com.icostel.arhitecturesample.utils.ImageRequestListener;
import com.icostel.arhitecturesample.view.model.User;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class UserDetailsActivity extends BaseActivity {

    private static final String TAG = UserDetailsActivity.class.getCanonicalName();

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.user_image)
    AppCompatImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        enableUpNavigation();
        toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());

        UserDetailsViewModel userDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel.class);
        String userId = getIntent().getStringExtra(Config.Data.USER_ID);
        if (TextUtils.isEmpty(userId)) {
            supportFinishAfterTransition();
        } else {
            userDetailsViewModel.init(userId);
            userDetailsViewModel.getUserLiveData().observe(this, this::bindUserData);
        }
    }

    private void bindUserData(@Nullable User user) {
        if (user != null) {
            GlideApp.with(this)
                    .load(user.getResourceUrl())
                    .transition(GenericTransitionOptions.with(AnimationFactory.getTransition(AnimationFactory.AnimationType.FADE_IN)))
                    .dontTransform()
                    .listener((ImageRequestListener) status -> Timber.e("%s, glide listener status: %s", TAG, status.toString()))
                    .into(userImage);
        }
    }
}


