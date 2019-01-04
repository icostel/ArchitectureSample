package com.icostel.arhitecturesample.ui.newuser

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.icostel.arhitecturesample.ui.newuser.NewUserViewModel.Const.IMAGE_REQUEST_CODE
import com.icostel.arhitecturesample.utils.livedata.SingleLiveEvent
import com.icostel.commons.navigation.IntentNavigationAction
import com.icostel.commons.navigation.NavigationAction
import timber.log.Timber
import javax.inject.Inject

class NewUserViewModel @Inject constructor() : ViewModel() {

    val navigationAction: SingleLiveEvent<NavigationAction> = SingleLiveEvent()

    fun onAddUser() {
        Timber.d("onAddUser()")
        //TODO add user to db using chain
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

    object Const {
        const val IMAGE_REQUEST_CODE = 1
    }
}