package com.icostel.arhitecturesample.ui.main;

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

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

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
        userAdapter = new UserAdapter(MainActivity.this);
        userRecyclerView.setAdapter(userAdapter);

        mainActivityViewModel.getUserListLiveData().observe(this, users -> userAdapter.updateUserList(users));
        mainActivityViewModel.getNavigatioActionLiveEvent().observe(this, this::navigateTo);
        userAdapter.getSelectedUserLive().observe(this, mainActivityViewModel::onUserSelected);
    }
}


