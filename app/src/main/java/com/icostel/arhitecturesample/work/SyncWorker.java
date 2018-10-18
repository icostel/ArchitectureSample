package com.icostel.arhitecturesample.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;

public class SyncWorker extends Worker {
    public SyncWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super();
    }

    @NonNull
    @Override
    public Result doWork() {
        // TODO work , sync users in DB
        return Result.SUCCESS;
    }

    // TODO define flags and other stuff used by this Worker
    private static class WorkerParameters {

    }
}
