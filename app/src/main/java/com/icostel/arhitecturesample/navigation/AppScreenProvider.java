package com.icostel.arhitecturesample.navigation;

import com.icostel.arhitecturesample.ui.listusers.UserListActivity;
import com.icostel.arhitecturesample.ui.loginuser.LoginUserActivity;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity;
import com.icostel.commons.navigation.ScreenProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

// one per app module
@Singleton
public class AppScreenProvider extends ScreenProvider {

    public static final int MAIN = 1;
    public static final int LIST_USERS = 2;
    public static final int USER_DETAILS = 3;


    @Inject
    AppScreenProvider() {
        super();
        initScreenMap();
    }

    @Override
    protected void initScreenMap() {
        SCREEN_MAP.put(MAIN, LoginUserActivity.class);
        SCREEN_MAP.put(LIST_USERS, UserListActivity.class);
        SCREEN_MAP.put(USER_DETAILS, UserDetailsActivity.class);
    }
}
