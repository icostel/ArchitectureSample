package com.icostel.arhitecturesample.navigation

import com.icostel.arhitecturesample.ui.loginuser.LoginUserActivity
import com.icostel.arhitecturesample.ui.main.MainActivity
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity
import com.icostel.arhitecturesample.ui.userdetails.UserDetailsActivity
import com.icostel.commons.navigation.ScreenProvider

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppScreenProvider @Inject
internal constructor() : ScreenProvider() {

    init {
        initScreenMap()
    }

    override fun initScreenMap() {
        SCREEN_MAP.put(LOGIN_USER, LoginUserActivity::class.java)
        SCREEN_MAP.put(NEW_USER, NewUserActivity::class.java)
        SCREEN_MAP.put(MAIN, MainActivity::class.java)
        SCREEN_MAP.put(USER_DETAILS, UserDetailsActivity::class.java)
    }

    companion object {

        const val LOGIN_USER = 1
        const val USER_DETAILS = 2
        const val NEW_USER = 4
        const val MAIN = 5
    }
}
