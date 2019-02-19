package com.icostel.arhitecturesample.ui.listusers

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.api.Status
import com.icostel.arhitecturesample.di.Injectable
import com.icostel.arhitecturesample.ui.BaseFragment
import com.icostel.arhitecturesample.utils.error.ErrorData
import com.icostel.arhitecturesample.utils.error.ErrorHandler
import com.icostel.arhitecturesample.utils.error.ErrorType
import com.icostel.arhitecturesample.view.model.User
import com.icostel.commons.navigation.Navigator
import com.icostel.commons.utils.OnQueryTextChangedListener
import com.icostel.commons.utils.bind
import com.icostel.commons.utils.extensions.observe
import kotlinx.android.synthetic.main.layout_user_list_start.*
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import javax.inject.Inject

class ListUsersFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var listUsersViewModel: ListUsersViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var addUserFab: FloatingActionButton
    private lateinit var emptyView: AppCompatTextView
    private lateinit var searchView: SearchView
    private lateinit var motionLayout: MotionLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        listUsersViewModel = ViewModelProviders.of(this, viewModelFactory).get(ListUsersViewModel::class.java)

        val fragView = inflater.inflate(R.layout.layout_user_list_start, container, false)

        userRecyclerView = fragView.bind(R.id.user_recycler)
        swipeRefreshLayout = fragView.bind(R.id.swipe_layout)
        addUserFab = fragView.bind(R.id.add_user_fab)
        emptyView = fragView.bind(R.id.empty_view)
        motionLayout = fragView.bind(R.id.motion_layout)

        userRecyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(activity)
        userRecyclerView.layoutManager = mLayoutManager
        userAdapter = UserAdapter(activity as Context)
        userRecyclerView.adapter = userAdapter
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary, activity!!.theme))

        listUsersViewModel.userListLiveData.observe(this) { userAdapter.updateItems(it as List<User>) }
        userAdapter.selectedItem.observe(this) { listUsersViewModel.onUserSelected(it as User) }
        swipeRefreshLayout.setOnRefreshListener { listUsersViewModel.refreshUsers(searchView.query.toString()) }
        listUsersViewModel.loadingStatus.observe(this) { this.handleLoadingStatus(it) }
        listUsersViewModel.navigationActionLiveEvent.observe(this) { (activity as Navigator).navigateTo(it) }

        addUserFab.setOnClickListener {
            val transitionBundle = ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                    addUserFab, "floating_btn_animation").toBundle()
            listUsersViewModel.onUserAdd(transitionBundle)
        }

        enableUpNavigation(false)
        setHasOptionsMenu(true)
        motionLayout.transitionToEnd()

        return fragView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.user_list_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.search_item)
        searchView = searchItem?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = getString(R.string.search_for_user)
        val searchCloseBtn: ImageView? = searchView.findViewById(R.id.search_close_btn)
        searchCloseBtn?.setImageDrawable(resources.getDrawable(R.drawable.ic_close_white_24dp, activity?.theme))
        searchCloseBtn?.setOnClickListener {
            if (!searchView.isIconified) {
                searchView.onActionViewCollapsed()
                setShouldHandleBack(false)
            }
        }
        searchView.setOnSearchClickListener {
            it.requestFocus()
            setShouldHandleBack(true)
        }
        searchView.setOnQueryTextListener((OnQueryTextChangedListener { listUsersViewModel.onSearchInput(it) }))

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onBackPress() {
        Timber.d("onBackPress()")

        if (shouldHandleBack()) {
            if (!searchView.isIconified) {
                setShouldHandleBack(false)
                searchView.onActionViewCollapsed()
            }
        }
    }

    override fun onOptionsItemSelected(@NotNull item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_item -> {
                searchView.onActionViewCollapsed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleLoadingStatus(status: Status.Type?) {
        Timber.d("handleLoadingStatus(): " + status?.name)

        status?.let {
            when (status) {
                Status.Type.IN_PROGRESS -> swipeRefreshLayout.isRefreshing = true
                Status.Type.SUCCESS -> {
                    emptyView.visibility = if (userAdapter.itemCount == 1) View.VISIBLE else View.GONE
                    user_recycler.visibility = if (userAdapter.itemCount == 1) View.GONE else View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }
                Status.Type.ERROR -> {
                    (activity as ErrorHandler).onUserErrorAction(
                            ErrorData(null,
                                    getString(R.string.failed_to_load_users),
                                    null,
                                    true,
                                    ErrorHandler.UserAction.Nothing,
                                    ErrorType.Error))
                    swipeRefreshLayout.isRefreshing = false
                }
                else -> swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
