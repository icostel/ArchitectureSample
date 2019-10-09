package com.icostel.arhitecturesample.work

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.icostel.arhitecturesample.di.factory.BaseWorkerFactory
import com.icostel.arhitecturesample.repository.UserRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SyncWorker constructor(
        context: Context,
        params: WorkerParameters,
        private val userRepository: UserRepository
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

    //TODO use additional injection helpers to not write the factory for each worker
    // google auto inject or square
    class Factory @Inject constructor(
            private val userRepoProvider: UserRepository
    ) : BaseWorkerFactory {
        override fun createWorker(appContext: Context,
                                  workerClassName: String,
                                  params: WorkerParameters): RxWorker {
            return SyncWorker(
                    appContext,
                    params,
                    userRepoProvider
            )
        }
    }

    companion object {
        const val WORK_OUTPUT_ERROR = "error"
        private val TAG = SyncWorker::class.java.simpleName
    }
}
