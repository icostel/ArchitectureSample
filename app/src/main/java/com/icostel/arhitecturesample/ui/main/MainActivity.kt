package com.icostel.arhitecturesample.ui.main

import android.os.Bundle
import android.view.ViewGroup
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
import com.icostel.commons.utils.bind
import timber.log.Timber

class MainActivity : BaseActivity() {

    companion object PagerScreens {
        const val NR_SCREENS = 2
        const val USERS = 0
        const val ABOUT = 1
    }

    private var viewPager: ViewPager? = null
    private var pagerAdapter: MainPagerAdapter? = null
    private var bottomNavigationView: BottomNavigationView? = null

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
                R.id.menu_user_list -> viewPager?.currentItem = USERS;
                R.id.menu_about -> viewPager?.currentItem = ABOUT;
            }
            true
        }
        hideTitle()
    }

    override fun onBackPressed() {
        val fragment = pagerAdapter?.currentFragment
        fragment?.let {
            if (fragment is BackFragment && fragment.shouldHandleBack()) {
                fragment.onBackPress()
            } else {
                if (fragment.childFragmentManager.backStackEntryCount > 1) {
                    fragment.childFragmentManager.popBackStack()
                } else {
                    super.onBackPressed()
                }
            }
        } ?: Timber.d("MainActivity::onBackPressed(), fragment not found")
    }

    private inner class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        internal var currentFragment: Fragment? = null

        override fun getCount(): Int = NR_SCREENS

        override fun getItem(position: Int): Fragment {
            return when (position) {
                USERS -> ListUsersFragment()
                ABOUT -> AboutFragment()
                else -> ListUsersFragment()
            }
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            super.setPrimaryItem(container, position, `object`)
            currentFragment = `object` as Fragment
        }
    }

}
