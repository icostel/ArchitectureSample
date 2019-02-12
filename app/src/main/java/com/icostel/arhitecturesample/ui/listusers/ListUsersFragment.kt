package com.icostel.arhitecturesample.ui.listusers

import android.app.Activity
import android.app.ActivityOptions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.api.SignInStatus
import com.icostel.arhitecturesample.di.Injectable
import com.icostel.arhitecturesample.di.ViewModelFactory
import com.icostel.arhitecturesample.ui.BaseFragment
import com.icostel.commons.navigation.Navigator
import com.icostel.commons.utils.bind
import com.icostel.commons.utils.extensions.observe
import javax.inject.Inject

class ListUsersFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var listUsersViewModel: ListUsersViewModel

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var addUserFab: FloatingActionButton
    private lateinit var emptyView: AppCompatTextView

    private var searchView: SearchView? = null
    private var userAdapter: UserAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        listUsersViewModel = ViewModelProviders.of(this, viewModelFactory).get(ListUsersViewModel::class.java)

        val fragView = inflater.inflate(R.layout.layout_user_list, container, false)

        userRecyclerView = fragView.bind(R.id.user_recycler)
        swipeRefreshLayout = fragView.bind(R.id.swipe_layout)
        addUserFab = fragView.bind(R.id.add_user_fab)
        emptyView = fragView.bind(R.id.empty_view)
        userRecyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(activity)
        userRecyclerView.layoutManager = mLayoutManager
        userAdapter = UserAdapter(activity)
        userRecyclerView.adapter = userAdapter
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary, activity!!.theme))

        listUsersViewModel.userListLiveData.observe(this) { userAdapter?.updateUserList(it) }
        userAdapter?.selectedUserLive?.observe(this) { listUsersViewModel.onUserSelected(it) }
        swipeRefreshLayout.setOnRefreshListener { listUsersViewModel.refreshUsers() }
        listUsersViewModel.loadingStatus.observe(this) { this.handleLoadingStatus(it) }
        listUsersViewModel.navigationActionLiveEvent.observe(this) { (activity as Navigator).navigateTo(it) }

        addUserFab.setOnClickListener {
            val transitionBundle = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                    addUserFab, "floating_btn_animation").toBundle()
            listUsersViewModel.onUserAdd(transitionBundle)
        }
        enableUpNavigation(true)

        return fragView
    }

    private fun handleLoadingStatus(status: SignInStatus.Status?) {

        status?.let {
            when (status) {
                SignInStatus.InProgress() -> swipeRefreshLayout.isRefreshing = true
                SignInStatus.Success() -> {
                    emptyView.visibility = if (userAdapter?.itemCount == 0) View.VISIBLE else View.GONE
                    swipeRefreshLayout.isRefreshing = false
                }
                SignInStatus.Error() -> {
                } //TODO handle error in the parent
                else -> swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
