package com.icostel.arhitecturesample.navigation;

import com.icostel.arhitecturesample.ui.loginuser.LoginUserActivity;
import com.icostel.arhitecturesample.ui.main.MainActivity;
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity;
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity;
import com.icostel.commons.navigation.ScreenProvider;

import javax.inject.Inject;

public class AppScreenProvider extends ScreenProvider {

    public static final int LOGIN_USER = 1;
    public static final int USER_DETAILS = 2;
    public static final int NEW_USER = 4;
    public static final int MAIN = 5;


    @Inject
    AppScreenProvider() {
        super();
        initScreenMap();
    }

    @Override
    protected void initScreenMap() {
        SCREEN_MAP.put(LOGIN_USER, LoginUserActivity.class);
        SCREEN_MAP.put(NEW_USER, NewUserActivity.class);
        SCREEN_MAP.put(MAIN, MainActivity.class);
        SCREEN_MAP.put(USER_DETAILS, UserDetailsActivity.class);
    }
}
