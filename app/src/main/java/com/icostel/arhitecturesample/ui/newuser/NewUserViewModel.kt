package com.icostel.arhitecturesample.ui.newuser

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.icostel.arhitecturesample.domain.UserHandler
import com.icostel.arhitecturesample.utils.livedata.SingleLiveEvent
import com.icostel.arhitecturesample.view.model.User
import com.icostel.arhitecturesample.view.model.UserMapper
import com.icostel.commons.navigation.IntentNavigationAction
import com.icostel.commons.navigation.NavigationAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NewUserViewModel @Inject constructor(private val userHandler: UserHandler, private val userMapper: UserMapper) : ViewModel() {

    val navigationAction: SingleLiveEvent<NavigationAction> = SingleLiveEvent()
    val apiResponse: MutableLiveData<Boolean> = MutableLiveData()
    private val apiDisposable: CompositeDisposable = CompositeDisposable()

    fun onAddUser(user: User) {
        Timber.d("onAddUser() %s", user.toString())

        apiDisposable.add(userHandler.addUser(userMapper.mapViewToDomain(user))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { success ->
                    Timber.d("%s onAddUser(): %b", TAG, success)
                    apiResponse.postValue(success)
                })
    }

    fun onAddUserImage() {
        Timber.d("onAddUserImage()")

        navigationAction.postValue(IntentNavigationAction.Builder()
                .setIntent(Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI))
                .setRequestCode(IMAGE_REQUEST_CODE)
                .setShouldFinish(false)
                .setType("image/*")
                .build())
    }

    override fun onCleared() {
        super.onCleared()

        if (apiDisposable.isDisposed) {
            apiDisposable.dispose()
        }
    }

    companion object {
        const val IMAGE_REQUEST_CODE = 1
        const val TAG = "NewUserViewModel"
    }
}