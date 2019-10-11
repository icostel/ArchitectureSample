package com.icostel.arhitecturesample.ui.screens.listusers

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.icostel.arhitecturesample.BuildConfig
import com.icostel.arhitecturesample.Config
import com.icostel.arhitecturesample.api.Status
import com.icostel.arhitecturesample.domain.usecases.UserUseCase
import com.icostel.arhitecturesample.navigation.AppScreenProvider
import com.icostel.arhitecturesample.ui.screens.newuser.NewUserActivity
import com.icostel.arhitecturesample.ui.mapper.UserMapper
import com.icostel.arhitecturesample.ui.model.User
import com.icostel.arhitecturesample.work.SyncWorker
import com.icostel.commons.BaseViewModel
import com.icostel.commons.navigation.ActivityNavigationAction
import com.icostel.commons.navigation.NavigationAction
import com.icostel.commons.utils.extensions.observe
import com.icostel.commons.utils.livedata.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ListUsersViewModel @Inject
internal constructor(private val context: Context,
                     private val userUseCase: UserUseCase,
                     private val userMapper: UserMapper,
                     private val appScreenProvider: AppScreenProvider
): BaseViewModel() {

    companion object {
        private val TAG = ListUsersViewModel::class.java.simpleName
    }

    // used in the UI for updating the user list
    internal val userListLiveData = MutableLiveData<List<User>>()
    internal val loadingStatus = MutableLiveData<Status.Type>()
    internal val navigationActionLiveEvent = SingleLiveEvent<NavigationAction>()

    init {
        this.userListLiveData.value = ArrayList()
        getUsers("")
    }

    internal fun getUsers(nameQuery: String) {
        loadingStatus.value = Status.Type.LOADING

        disposable.add(userUseCase.getAllUsers(nameQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1000, TimeUnit.MILLISECONDS) // just for testing lag
                .subscribe({ userList ->
                    userListLiveData.postValue(userMapper.mapDomainToView(userList))
                    loadingStatus.postValue(Status.Type.SUCCESS)
                    if (BuildConfig.DEBUG) {
                        userListLiveData.value?.let { uList ->
                            for (u in uList) {
                                Timber.d("user: $u")
                            }
                        }
                    }
                }, { throwable ->
                    Timber.e("Could not get users: $throwable")
                    loadingStatus.setValue(Status.Type.ERROR)
                }))
    }

    internal fun getUsersWithWorker(owner: LifecycleOwner) {
        loadingStatus.value = Status.Type.LOADING
        val uuid = userUseCase.refreshUsersFromApi()
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(uuid).observe(owner) { workInfo ->
            if (workInfo != null) {
                // we got the new users and are new available in the db so call that
                // as we can't pass lists in Workinfo.Outputdata
                when(workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        Timber.d("$TAG getUsersWithWorker() SUCCEEDED")
                    }
                    WorkInfo.State.RUNNING -> {
                        Timber.d("$TAG getUsersWithWorker() RUNNING")
                        // the loading spinner will be canceled by the api call
                        getUsers("")
                    }
                    WorkInfo.State.FAILED -> {
                        Timber.d("$TAG getUsersWithWorker() FAILED, err:" +
                                workInfo.outputData.getString(SyncWorker.WORK_OUTPUT_ERROR))
                        loadingStatus.value = Status.Type.ERROR
                    }
                    else -> {}
                }
            }
        }
    }

    fun onUserAdd(transitionBundle: Bundle) {
        Timber.d("onUserAdd()")

        navigationActionLiveEvent.postValue(ActivityNavigationAction.Builder()
                .setScreenProvider(appScreenProvider)
                .setScreen(AppScreenProvider.NEW_USER)
                .setRequestCode(NewUserActivity.REQUEST_CODE_USER_ADDED)
                .setTransitionBundle(transitionBundle)
                .setShouldFinish(false)
                .build())
    }

    // navigate to details when the user selects a specific user from the list
    internal fun onUserSelected(user: User) {
        Timber.d("onUserSelected()")

        val args = Bundle()
        args.putString(Config.Data.USER_ID, user.id)
        navigationActionLiveEvent.postValue(
                ActivityNavigationAction.Builder()
                        .setScreen(AppScreenProvider.USER_DETAILS)
                        .setBundle(args)
                        .setShouldFinish(false)
                        .setScreenProvider(appScreenProvider)
                        .build())
    }
}
