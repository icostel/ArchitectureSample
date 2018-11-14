package com.icostel.arhitecturesample.ui.loginuser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.api.SignInStatus;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;
import com.icostel.arhitecturesample.utils.error.ErrorData;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginUserActivity extends BaseActivity {
    @Inject
    ViewModelFactory viewModelFactory;

    LoginUserViewModel loginUserViewModel;

    @BindView(R.id.login_btn)
    Button loginBtn;

    @BindView(R.id.user_email_tv)
    TextView userEmailTv;

    @BindView(R.id.user_pass_tv)
    TextView userPassTv;

    @BindView(R.id.loading_dialog)
    ProgressBar loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_user);
        ButterKnife.bind(this);
        loginUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginUserViewModel.class);
        loginUserViewModel.getNavigationAction().observe(this, this::navigateTo);
        loginUserViewModel.getSignInStatusLive().observe(this, this::handleLoginStatus);
        loginBtn.setOnClickListener(v -> loginUserViewModel.onLogInBtnClicked(userEmailTv.getText().toString(), userPassTv.getText().toString()));
    }

    private void handleLoginStatus(SignInStatus.Status status) {
        switch (status) {
            case IN_PROGRESS:
                loadingDialog.setVisibility(View.VISIBLE);
                loginBtn.setEnabled(false);
                break;
            case INPUTS_ERROR:
                Toast.makeText(LoginUserActivity.this, R.string.inputs_invalid, Toast.LENGTH_SHORT).show();
                loadingDialog.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
                break;
            case CALL_ERROR:
                Toast.makeText(LoginUserActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                loadingDialog.setVisibility(View.GONE);
                loginBtn.setEnabled(true);
                break;
            case SUCCESS:
                Toast.makeText(LoginUserActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                loadingDialog.setVisibility(View.GONE);
                loginUserViewModel.onLoginSuccess();
                loginBtn.setEnabled(true);
                break;

        }
    }
}
