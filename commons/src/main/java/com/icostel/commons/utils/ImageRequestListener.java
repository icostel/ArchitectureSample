package com.icostel.commons.utils;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Custom image request listener to be used for error handling/debug as a lambda call
 */
public interface ImageRequestListener extends RequestListener {
    void loadStatus(GlideStatus glideStatus);

    @Override
    default boolean onLoadFailed(@Nullable GlideException glideException, Object model, Target target, boolean isFirstResource) {
        loadStatus(new GlideStatus(glideException));
        return false;
    }

    @Override
    default boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        loadStatus(new GlideStatus("onResourceReady"));
        return false;
    }

    // wraps a glide exception as a status
    class GlideStatus {
        private GlideException status;

        GlideStatus(String msg) {
            this.status = new GlideException(msg);
        }

        GlideStatus(GlideException exception) {
            this.status = exception == null ? this.status = new GlideException("exception")
                    : new GlideException(exception.getMessage());
        }

        @NonNull
        @Override
        public String toString() {
            return status == null ? "unknown" : status.getMessage();
        }
    }
}
