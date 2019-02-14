package com.icostel.commons.utils;

import android.content.Intent;

public class IntentUtils {

    public static final int IMAGE_REQUEST_CODE = 1;

    public static class IntentFactory {

        public static Intent getImagePickerIntent() {
            return new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        }
    }
}
