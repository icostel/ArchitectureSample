package com.icostel.arhitecturesample.ui.userdetails;

import android.os.Bundle;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.InjectableActivity;
import com.icostel.arhitecturesample.di.ViewModelFactory;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import timber.log.Timber;

public class UserDetailsActivity extends InjectableActivity {

    private static final String TAG = UserDetailsActivity.class.getCanonicalName();
    public static final String EXTRA_USER_ID = "com.icostel.arhitecturesample.EXTRA_USER_ID";

    @Inject
    ViewModelFactory viewModelFactory;

    private UserDetailsViewModel userDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        ButterKnife.bind(this);
        userDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel.class);
        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        Timber.d(TAG + "User details for user with id %s", userId);
    }
}


