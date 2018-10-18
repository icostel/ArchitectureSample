package com.icostel.arhitecturesample.ui;

import android.os.Bundle;

import com.icostel.arhitecturesample.R;
import com.icostel.arhitecturesample.di.InjectableActivity;
import com.icostel.arhitecturesample.di.ViewModelFactory;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends InjectableActivity {

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
    }


    /*
    private enum TargetFragment implements FragmentNavigationAction.TargetFragment {
        NewFragment1, NewFragment2;

        @Override
        public Class<? extends Fragment> getFragmentClass() {
            switch (this) {
                case NewFragment1:
                    return NewFragment1.class;
                case NewFragment2:
                default:
                    return NewFragment2.class;
            }
        }
    }
    */
}


