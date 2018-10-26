package com.icostel.arhitecturesample.ui.listusers;

import android.os.Bundle;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.ViewModelFactory;
import com.icostel.arhitecturesample.ui.BaseActivity;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListUsersActivity extends BaseActivity {

    private static final String TAG = ListUsersActivity.class.getCanonicalName();

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.user_recycler)
    RecyclerView userRecyclerView;

    private ListUsersViewModel listUsersViewModel;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        listUsersViewModel = ViewModelProviders.of(this, viewModelFactory).get(ListUsersViewModel.class);

        userRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(mLayoutManager);
        userAdapter = new UserAdapter(ListUsersActivity.this);
        userRecyclerView.setAdapter(userAdapter);

        listUsersViewModel.getUserListLiveData().observe(this, users -> userAdapter.updateUserList(users));
        listUsersViewModel.getNavigationActionLiveEvent().observe(this, this::navigateTo);
        userAdapter.getSelectedUserLive().observe(this, listUsersViewModel::onUserSelected);
    }
}


