package com.icostel.arhitecturesample.ui.newuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.icostel.arhitecturesample.domain.UserHandler
import com.icostel.arhitecturesample.view.model.User
import com.icostel.arhitecturesample.view.mapper.UserMapper
import com.icostel.commons.navigation.IntentNavigationAction
import com.icostel.commons.navigation.NavigationAction
import com.icostel.commons.utils.IntentUtils
import com.icostel.commons.utils.isNotDigitsOnly
import com.icostel.commons.utils.livedata.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NewUserViewModel @Inject constructor(private val userHandler: UserHandler, private val userMapper: UserMapper) : ViewModel() {

    val navigationAction: SingleLiveEvent<NavigationAction> = SingleLiveEvent()
    val apiResponse: MutableLiveData<Boolean> = MutableLiveData()
    val inputValidation: MutableLiveData<Int> = MutableLiveData()
    val allDataAvailable: MutableLiveData<Boolean> = MutableLiveData()

    private val apiDisposable: CompositeDisposable = CompositeDisposable()

    init {
        allDataAvailable.postValue(false)
    }

    fun onAddUser(user: User) {
        Timber.d("onAddUser() %s", user.toString())

        if (validateData(user)) {
            apiDisposable.add(userHandler.addUser(userMapper.mapViewToDomain(user))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { success ->
                        Timber.d("%s onAddUser(): %b", TAG, success)
                        apiResponse.postValue(success)
                    })
        }
    }

    fun allDataAvailable(user: User) {
        allDataAvailable.postValue(
                user.firstName.isNotEmpty()
                && user.lastName.isNotEmpty()
                && user.age.isNotEmpty()
                && user.country.isNotEmpty()
                && user.age.isNotEmpty()
        )
    }

    // error handling after submitting
    private fun validateData(user: User): Boolean {
        var ret = false

        if (user.firstName.isEmpty()) {
            inputValidation.postValue(ERROR_FIRST_NAME)
        } else if (user.lastName.isEmpty()) {
            inputValidation.postValue(ERROR_LAST_NAME)
        } else if (user.country.isEmpty()) {
            inputValidation.postValue(ERROR_COUNTRY)
        } else if (user.age.isEmpty() || user.age.isNotDigitsOnly()) {
            inputValidation.postValue(ERROR_AGE)
        } else {
            Timber.d("user data valid")
            ret = true
        }

        return ret
    }

    fun onAddUserImage() {
        Timber.d("onAddUserImage()")

        navigationAction.postValue(IntentNavigationAction.Builder()
                .setIntent(IntentUtils.IntentFactory.getImagePickerIntent())
                .setRequestCode(IntentUtils.IMAGE_REQUEST_CODE)
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
        const val TAG = "NewUserViewModel"

        const val ERROR_FIRST_NAME = 1
        const val ERROR_LAST_NAME = 2
        const val ERROR_COUNTRY = 3
        const val ERROR_AGE = 4
    }
}