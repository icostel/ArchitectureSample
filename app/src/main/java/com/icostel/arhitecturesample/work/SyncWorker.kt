package com.icostel.arhitecturesample.work

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import com.icostel.arhitecturesample.di.factory.BaseWorkerFactory
import com.icostel.arhitecturesample.repository.UserRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@AutoFactory(
        className = "SyncWorkerFactory",
        allowSubclasses = true,
        implementing = [ BaseWorkerFactory::class ])
class SyncWorker constructor(
        context: Context,
        params: WorkerParameters,
        @Provided private val userRepository: UserRepository
) : RxWorker(context, params) {

    override fun createWork(): Single<Result> {
        return Single.fromCallable {userRepository.storeUsersInDb(userRepository.getUsersFromApi())}
            .subscribeOn(Schedulers.io())
            .doOnSuccess { Timber.d("$TAG success") }
            .map { Result.success() }
            .doOnError {
                Timber.d("$TAG failed with ${it.message}")
                Result.failure(workDataOf(WORK_OUTPUT_ERROR to it.message))
            }
    }

    companion object {
        const val WORK_OUTPUT_ERROR = "error"
        private val TAG = SyncWorker::class.java.simpleName
    }
}
