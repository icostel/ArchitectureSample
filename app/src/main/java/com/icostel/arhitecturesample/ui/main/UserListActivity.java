package com.icostel.arhitecturesample.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.api.utils.SignInStatus;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity {

    private static final String TAG = UserListActivity.class.getCanonicalName();

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.user_recycler)
    RecyclerView userRecyclerView;

    private MainActivityViewModel mainActivityViewModel;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel.class);

        userRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(mLayoutManager);
        userAdapter = new UserAdapter(UserListActivity.this);
        userRecyclerView.setAdapter(userAdapter);

        mainActivityViewModel.getUserListLiveData().observe(this, users -> userAdapter.updateUserList(users));
        mainActivityViewModel.getNavigationActionLiveEvent().observe(this, this::navigateTo);
        //TODO move this to a user login activity and view model
        mainActivityViewModel.getSignInStatusLive().observe(this, signInStatus ->
                Toast.makeText(this, signInStatus == SignInStatus.Status.Success ? R.string.logged_in_success : R.string.logged_in_error, Toast.LENGTH_SHORT));
        userAdapter.getSelectedUserLive().observe(this, mainActivityViewModel::onUserSelected);
    }
}


