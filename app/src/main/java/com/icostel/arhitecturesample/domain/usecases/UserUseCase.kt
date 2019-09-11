package com.icostel.arhitecturesample.domain.usecases

import com.icostel.arhitecturesample.domain.model.User
import com.icostel.arhitecturesample.domain.model.UserMapper
import com.icostel.arhitecturesample.repository.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

class UserUseCase @Inject
internal constructor(private val userRepository: UserRepository,
                     private val userMapper: UserMapper): BaseUseCase() {

    fun getAllUsers(nameQuery: String): Observable<List<User>> {
        return userRepository.getAllUsers(nameQuery)
            .map { repoUsers -> userMapper.mapApiUsersToDomain(repoUsers) }
    }

    fun getUser(userId: String): Observable<User> {
        return userRepository.getUserById(userId)
            .map { repoUser -> userMapper.mapApiUserToDomain(repoUser) }
    }

    fun addUser(user: User): Observable<Boolean> {
        return userRepository.addUser(userMapper.mapDomainToApi(user))
    }

}
