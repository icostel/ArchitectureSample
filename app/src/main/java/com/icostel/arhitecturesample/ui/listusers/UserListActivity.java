package com.icostel.arhitecturesample.ui.listusers;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.api.SignInStatus;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity;
import com.icostel.arhitecturesample.utils.OnQueryTextChangedListener;
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

        // live data
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
        showTitle(true);
    }

    private void handleLoadingStatus(SignInStatus.Status status) {

        switch (status) {
            case IN_PROGRESS:
                swipeRefreshLayout.setRefreshing(true);
                break;
            case SUCCESS:
                emptyView.setVisibility(userAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                userRecyclerView.setVisibility(userAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                break;
            case ERROR:
                showError(new ErrorData("error", getString(R.string.error_loading_users), "", false, null, ErrorType.Error));
                swipeRefreshLayout.setRefreshing(false);
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
        ImageView searchCloseBtn = searchView.findViewById(R.id.search_close_btn);
        searchCloseBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_white_24dp, getTheme()));
        searchCloseBtn.setOnClickListener(v -> {
            if (!searchView.isIconified()) {
                searchView.onActionViewCollapsed();
                showTitle(true);
            }
        });
        searchView.setOnSearchClickListener(v -> {
            showTitle(false);
            v.requestFocus();
        });
        searchView.setOnQueryTextListener((OnQueryTextChangedListener) newText ->
                listUsersViewModel.onSearchInput(newText));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                searchView.onActionViewCollapsed();
                showTitle(true);
                return true;
            case android.R.id.home:
                if (!searchView.isIconified()) {
                    searchView.onActionViewCollapsed();
                    showTitle(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            showTitle(true);
        } else {
            super.onBackPressed();
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


