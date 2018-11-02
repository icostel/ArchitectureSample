package com.icostel.arhitecturesample.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;

@Singleton
public class AppExecutors {

    private static final int NETWORK_THREADS = 3;

    private Executor diskIO;
    private Executor networkIO;
    private Executor mainThread;
    private Executor background;

    @Inject
    AppExecutors() {
        diskIO = Executors.newSingleThreadExecutor();
        networkIO = Executors.newFixedThreadPool(NETWORK_THREADS);
        mainThread = new MainThreadExecutor();
        background = Executors.newSingleThreadExecutor();
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor background() {
        return background;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}

