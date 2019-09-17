package com.icostel.arhitecturesample.view.mapper

import com.icostel.arhitecturesample.view.model.User
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Used for mapping models from domain <-> view
 */

@Singleton
class UserMapper @Inject constructor() {

    fun mapDomainToView(dbUser: com.icostel.arhitecturesample.domain.model.User): User {
        return User(dbUser.id, dbUser.firstName, dbUser.lastName, dbUser.resourceUrl, dbUser.country, dbUser.age)
    }

    fun mapViewToDomain(viewModel: User): com.icostel.arhitecturesample.domain.model.User {
        return com.icostel.arhitecturesample.domain.model.User(viewModel.id, viewModel.firstName, viewModel.lastName, viewModel.resourceUrl, viewModel.country, viewModel.age)
    }

    fun mapViewToDomain(viewModels: List<User>?): List<com.icostel.arhitecturesample.domain.model.User> {
        val domainUsers = ArrayList<com.icostel.arhitecturesample.domain.model.User>()
        viewModels?.forEach {
            domainUsers.add(mapViewToDomain(it))
        }
        return domainUsers
    }

    fun mapDomainToView(apiUsers: List<com.icostel.arhitecturesample.domain.model.User>?): List<User> {
        val viewUsers = ArrayList<User>()
        apiUsers?.forEach {
            viewUsers.add(mapDomainToView(it))
        }
        return viewUsers
    }
}
