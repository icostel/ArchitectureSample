package com.icostel.arhitecturesample.utils.error;

import com.icostel.arhitecturesample.utils.livedata.SingleLiveEvent;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
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

    LiveData<Boolean> isVisible() {
        return Transformations.map(userAction, data ->
                data.getUserAction() != ErrorHandler.UserAction.Button
                        && data.getUserAction() != ErrorHandler.UserAction.Dismiss);
    }

    void makeError(@NonNull ErrorData errorData) {
        errorData.setUserAction(ErrorHandler.UserAction.Nothing);
        error.setValue(errorData);
    }

    void onButtonClicked(@NonNull ErrorData errorData) {
        errorData.setUserAction(ErrorHandler.UserAction.Button);
        userAction.setValue(errorData);
    }

    void onDismissClicked(@NonNull ErrorData errorData) {
        errorData.setUserAction(ErrorHandler.UserAction.Dismiss);
        userAction.setValue(errorData);
    }

    boolean shouldDismissError(@NonNull String tag) {
        ErrorData errorData = error.getValue();
        if (errorData != null && tag.equals(errorData.getTag())) {
            onDismissClicked(errorData);
            return true;
        }
        return false;
    }
}
