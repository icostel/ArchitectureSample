package com.icostel.arhitecturesample.ui.userdetails;

import android.os.Bundle;

import com.icostel.arhitecturesample.Config;
import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;

import javax.inject.Inject;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        ButterKnife.bind(this);
        UserDetailsViewModel userDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel.class);
        String userId = getIntent().getStringExtra(Config.Data.USER_ID);
        //TODO load user and user details from db based on id, create userdetails models and
        enableUpNavigation();
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}


