package com.icostel.arhitecturesample.utils.error;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ErrorData implements Parcelable {
    String errMsg;
    boolean shouldAutoDismiss;

    ErrorHandler.UserAction errorUserAction;

    public ErrorData(@NonNull String errMsg, boolean shouldAutoDismiss) {
        this.errMsg = errMsg;
        this.shouldAutoDismiss = shouldAutoDismiss;
    }

    private ErrorData(Parcel in) {
        errMsg = in.readString();
        shouldAutoDismiss = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(errMsg);
        dest.writeByte((byte) (shouldAutoDismiss ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ErrorData> CREATOR = new Parcelable.Creator<ErrorData>() {
        @Override
        public ErrorData createFromParcel(Parcel in) {
            return new ErrorData(in);
        }

        @Override
        public ErrorData[] newArray(int size) {
            return new ErrorData[size];
        }
    };
}
