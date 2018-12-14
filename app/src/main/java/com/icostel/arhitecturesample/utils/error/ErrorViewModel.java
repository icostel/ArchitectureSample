package com.icostel.arhitecturesample.utils.error;

import com.icostel.arhitecturesample.utils.livedata.SingleLiveEvent;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ErrorViewModel extends ViewModel {
    private final MutableLiveData<ErrorData> error = new MutableLiveData<>();
    private final SingleLiveEvent<ErrorData> userAction = new SingleLiveEvent<>();

    @Inject
    public ErrorViewModel() {

    }

    LiveData<ErrorData> getError() {
        return error;
    }

    public LiveData<ErrorData> getUserAction() {
        return userAction;
    }

    void makeError(@NonNull ErrorData errorData) {
        errorData.errorUserAction = ErrorHandler.UserAction.Nothing;
        error.setValue(errorData);
    }

    void onDismissClicked(@NonNull ErrorData errorData) {
        errorData.errorUserAction = ErrorHandler.UserAction.Dismiss;
        userAction.setValue(errorData);
    }
}
