package com.icostel.arhitecturesample.ui.listusers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.api.SignInStatus;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity;
import com.icostel.arhitecturesample.utils.error.ErrorData;
import com.icostel.arhitecturesample.utils.error.ErrorHandler;
import com.icostel.arhitecturesample.utils.error.ErrorType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
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

    @BindView(R.id.empty_view)
    AppCompatTextView emptyView;

    private SearchView searchView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_user_list);

        ButterKnife.bind(this);
        listUsersViewModel = ViewModelProviders.of(this, viewModelFactory).get(ListUsersViewModel.class);

        userRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(mLayoutManager);
        userAdapter = new UserAdapter(UserListActivity.this);
        userRecyclerView.setAdapter(userAdapter);

        listUsersViewModel.getUserListLiveData().observe(this, users -> {
            userAdapter.updateUserList(users);
            emptyView.setVisibility(users.size() == 0 ? View.VISIBLE : View.GONE);
        });
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
        showTitle(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search_for_user));
        searchView.setOnSearchClickListener(v -> {
            enableUpNavigation(false);
            showTitle(false);
            v.requestFocus();
        });
        searchView.setOnCloseListener(() -> {
            enableUpNavigation(true);
            showTitle(true);
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.search_item) {
            searchView.onActionViewCollapsed();
            showTitle(true);
            enableUpNavigation(false);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showTitle(boolean showTitle) {
        if (showTitle) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.user_list_label);
            }
        } else {
            hideTitle();
        }
    }

    @Override
    public void onUserErrorAction(@Nullable ErrorData errorData) {
        //TODO handle error dialogs user input if any
    }
}


