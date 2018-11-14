package com.icostel.arhitecturesample.ui.listusers;

import android.os.Bundle;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.api.SignInStatus;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;
import com.icostel.arhitecturesample.utils.error.ErrorData;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.user_recycler)
    RecyclerView userRecyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private UserAdapter userAdapter;
    private ListUsersViewModel listUsersViewModel;

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
        listUsersViewModel.getLoadingStatus().observe(this, this::handleLoadingStatus);

        enableUpNavigation();
    }

    private void handleLoadingStatus(SignInStatus.Status status) {
        swipeRefreshLayout.setRefreshing(status == SignInStatus.Status.IN_PROGRESS);

        switch (status) {
            case ERROR:
                showError(getString(R.string.error_loading_users), true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onUserErrorAction(@Nullable ErrorData errorData) {
        super.onUserErrorAction(errorData);
        //TODO
    }
}


