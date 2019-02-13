package com.icostel.arhitecturesample.ui.loginuser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.api.Status;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;
import com.icostel.commons.utils.AfterTextChangeListener;
import com.icostel.arhitecturesample.utils.error.ErrorData;
import com.icostel.arhitecturesample.utils.error.ErrorHandler;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class LoginUserActivity extends BaseActivity implements ErrorHandler {
    @Inject
    ViewModelFactory viewModelFactory;

    LoginUserViewModel loginUserViewModel;

    private Button loginBtn;
    private TextView userEmailTv;
    private TextView userPassTv;
    private ProgressBar loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_user);

        loginBtn = findViewById(R.id.login_btn);
        userEmailTv = findViewById(R.id.user_email_tv);
        userPassTv = findViewById(R.id.user_pass_tv);
        loadingDialog = findViewById(R.id.loading_dialog);

        loginUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginUserViewModel.class);
        userEmailTv.addTextChangedListener((AfterTextChangeListener) input -> loginUserViewModel.allInputsAvailable(userEmailTv.getText(), userPassTv.getText()));
        userPassTv.addTextChangedListener((AfterTextChangeListener) input -> loginUserViewModel.allInputsAvailable(userEmailTv.getText(), userPassTv.getText()));
        loginUserViewModel.getNavigationAction().observe(this, this::navigateTo);
        loginUserViewModel.getSignInStatusLive().observe(this, this::handleLoginStatus);
        loginUserViewModel.getAllInputsAvailable().observe(this, loginBtn::setEnabled);
        loginBtn.setOnClickListener(v -> loginUserViewModel.onLogInBtnClicked(userEmailTv.getText().toString(), userPassTv.getText().toString()));
    }

    private void handleLoginStatus(Status.Type type) {
        switch (type) {
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

    @Override
    public void onUserErrorAction(@org.jetbrains.annotations.Nullable ErrorData errorData) {
        //TODO handle error dialogs user input if any
    }
}
