package com.icostel.arhitecturesample.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.ui.BackFragment
import com.icostel.arhitecturesample.ui.BaseActivity
import com.icostel.arhitecturesample.ui.about.AboutFragment
import com.icostel.arhitecturesample.ui.listusers.ListUsersFragment
import com.icostel.arhitecturesample.ui.newuser.NewUserActivity
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorHandler
import com.icostel.commons.utils.bind
import timber.log.Timber

class MainActivity : BaseActivity(), ErrorHandler {

    companion object PagerScreens {
        const val NR_SCREENS = 2
        const val USERS = 0
        const val ABOUT = 1
    }

    private var viewPager: ViewPager? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ui bindings
        setContentView(R.layout.layout_main)
        viewPager = bind(R.id.main_view_pager)
        bottomNavigationView = bind(R.id.bottomNavigation)

        // pager setup
        viewPager?.adapter = MainPagerAdapter(supportFragmentManager)

        bottomNavigationView?.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_user_list -> viewPager?.currentItem = USERS
                R.id.menu_about -> viewPager?.currentItem = ABOUT
            }
            true
        }
        hideTitle()
    }

    override fun onBackPressed() {
        currentFragment = supportFragmentManager.fragments[0]
        currentFragment?.let { frag ->
            if (frag is BackFragment) {
                Timber.d("back fragment found")
                if (frag.shouldHandleBack()) {
                    frag.onBackPress()
                } else {
                    Timber.d("fragment does not handle back")
                    super.onBackPressed()
                }
            } else {
                Timber.d("fragment is not back fragment")
                super.onBackPressed()
            }
        } ?: run {
            Timber.d("no current fragment available")
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NewUserActivity.REQUEST_CODE_USER_ADDED) {
                (supportFragmentManager.fragments[USERS] as ListUsersFragment).refreshUsers()
            }
        }
    }

    private inner class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int = NR_SCREENS

        override fun getItem(position: Int): Fragment {
            return when (position) {
                USERS -> ListUsersFragment()
                ABOUT -> AboutFragment()
                else -> ListUsersFragment()
            }
        }
    }

    override fun onUserErrorAction(errorData: ErrorData?) {
        showError(errorData)
    }

}
