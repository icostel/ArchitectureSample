package com.icostel.arhitecturesample.work

import android.content.Context
import androidx.work.*
import com.icostel.arhitecturesample.di.factory.BaseWorkerFactory
import com.icostel.arhitecturesample.repository.UserRepository
import io.reactivex.Single
import timber.log.Timber

import javax.inject.Inject
import javax.inject.Provider

class SyncWorker constructor(
        context: Context,
        params: WorkerParameters,
        private val userRepository: UserRepository)
    : RxWorker(context, params) {

    override fun createWork(): Single<Result> {
        val query: String? = inputData.getString(WORK_INPUT_QUERY)
        return Single.just(userRepository.getAllUsers(query ?: ""))
                .doOnSuccess { Timber.d("$TAG success")}
                .map { Result.success(workDataOf(WORK_OUTPUT_USERS to it)) }
                .doOnError {
                    Timber.d("$TAG failed with ${it.message}")
                    Result.failure(workDataOf(WORK_OUTPUT_ERROR to it.message))
                }
    }

    //TODO use additional injection helpers to not write the factory for each worker
    class Factory @Inject constructor(
            private val userRepoProvider: Provider<UserRepository>
    ) : BaseWorkerFactory {
        override fun createWorker(appContext: Context,
                                  workerClassName: String,
                                  params: WorkerParameters): RxWorker {
            return SyncWorker(
                    appContext,
                    params,
                    userRepoProvider.get()
            )
        }
    }

    companion object {
        const val WORK_INPUT_QUERY = "query"
        const val WORK_OUTPUT_USERS = "user_list"
        const val WORK_OUTPUT_ERROR = "error"
        private val TAG = SyncWorker::class.java.simpleName
    }
}
