package com.icostel.arhitecturesample.ui.listusers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.api.SignInStatus;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity;
import com.icostel.arhitecturesample.utils.error.ErrorData;
import com.icostel.arhitecturesample.utils.error.ErrorHandler;
import com.icostel.arhitecturesample.utils.error.ErrorType;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class UserListActivity extends BaseActivity implements ErrorHandler {

    @Inject
    ViewModelFactory viewModelFactory;

    ListUsersViewModel listUsersViewModel;

    @BindView(R.id.user_recycler)
    RecyclerView userRecyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.add_user_fab)
    FloatingActionButton addUserFab;

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        listUsersViewModel = ViewModelProviders.of(this, viewModelFactory).get(ListUsersViewModel.class);

        userRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(mLayoutManager);
        userAdapter = new UserAdapter(UserListActivity.this);
        userRecyclerView.setAdapter(userAdapter);

        listUsersViewModel.getUserListLiveData().observe(this, users -> userAdapter.updateUserList(users));
        listUsersViewModel.getNavigationActionLiveEvent().observe(this, this::navigateTo);
        userAdapter.getSelectedUserLive().observe(this, listUsersViewModel::onUserSelected);
        swipeRefreshLayout.setOnRefreshListener(listUsersViewModel::refreshUsers);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary, getTheme()));
        listUsersViewModel.getLoadingStatus().observe(this, this::handleLoadingStatus);
        addUserFab.setOnClickListener(v -> {
            Bundle transitionBundle = ActivityOptions.makeSceneTransitionAnimation((Activity) getContext(),
                    addUserFab, "floating_btn_animation").toBundle();
            listUsersViewModel.onUserAdd(transitionBundle);
        });

        enableUpNavigation();
        hideTitle();
    }

    private void handleLoadingStatus(SignInStatus.Status status) {
        swipeRefreshLayout.setRefreshing(status == SignInStatus.Status.IN_PROGRESS);

        switch (status) {
            case ERROR:
                showError(new ErrorData("error", getString(R.string.error_loading_users), "", false, null, ErrorType.Error));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("onActivityResult(), requestCode: %d, resultCode: %d", requestCode, resultCode);

        if (requestCode == NewUserActivity.RESULT_CODE_USER_ADDED
                && resultCode == Activity.RESULT_OK) {
            listUsersViewModel.refreshUsers();
        }
    }

    @Override
    public void onUserErrorAction(@Nullable ErrorData errorData) {
        //TODO handle error dialogs user input if any
    }
}


