package com.icostel.arhitecturesample.domain.usecases

import android.content.Context
import androidx.work.*
import com.icostel.arhitecturesample.domain.mapper.UserMapper
import com.icostel.arhitecturesample.domain.model.User
import com.icostel.arhitecturesample.repository.UserRepository
import com.icostel.arhitecturesample.work.SyncWorker
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

//TODO break this into multiple use cases
//getAllUsersUseCase
//getUserUseCase so on...
class UserUseCase @Inject
internal constructor(
        private val context: Context,
        private val userRepository: UserRepository,
        private val userMapper: UserMapper)
    : BaseUseCase {

    fun getAllUsers(nameQuery: String): Observable<List<User>> {
        return userRepository.getAllUsersObservable(nameQuery)
                .map { repoUsers -> userMapper.mapApiUsersToDomain(repoUsers) }
    }

    fun getUser(userId: String): Observable<User> {
        return userRepository.getUserById(userId)
                .map { repoUser -> userMapper.mapApiUserToDomain(repoUser) }
    }

    fun addUser(user: User): Observable<Boolean> {
        return userRepository.addUser(userMapper.mapDomainToApi(user))
    }

    fun refreshUsersFromApi(): UUID {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val syncUsersRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(context).enqueue(syncUsersRequest)
        return syncUsersRequest.id
    }
}
