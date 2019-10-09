package com.icostel.arhitecturesample.domain.mapper

import com.icostel.arhitecturesample.domain.model.User
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

/**
 * Used for mapping from api/db to domain and from domain to view models
 */

@Singleton
class UserMapper @Inject constructor() {

    fun mapDomainToApi(domainUser: User): com.icostel.arhitecturesample.api.model.User {
        return com.icostel.arhitecturesample.api.model.User(
                domainUser.id,
                domainUser.firstName,
                domainUser.lastName,
                domainUser.resourceUrl,
                domainUser.country,
                domainUser.age
        )
    }

    fun mapDomainUsersToApi(domainUsers: List<User>?): List<com.icostel.arhitecturesample.api.model.User> {
        val apiUsers = ArrayList<com.icostel.arhitecturesample.api.model.User>()
        domainUsers?.forEach { domainUser ->
            apiUsers.add(com.icostel.arhitecturesample.api.model.User(
                    id = domainUser.id,
                    firstName = domainUser.firstName,
                    lastName = domainUser.lastName,
                    resourceUrl = domainUser.resourceUrl,
                    country = domainUser.country,
                    age = domainUser.age
            ))
        }
        return apiUsers
    }

    fun mapApiUsersToDomain(apiUsers: List<com.icostel.arhitecturesample.api.model.User>?): List<User> {
        val domainUsers = ArrayList<User>()
        apiUsers?.forEach { apiUser ->
            domainUsers.add(User(
                    id = apiUser.id,
                    firstName = apiUser.firstName,
                    lastName = apiUser.lastName,
                    resourceUrl = apiUser.resourceUrl,
                    country = apiUser.country,
                    age = apiUser.age
            ))
        }
        return domainUsers
    }

    fun mapApiUserToDomain(apiUser: Optional<com.icostel.arhitecturesample.api.model.User>): User {
        return if (apiUser.isPresent) {
            apiUser.get().run {
                User(
                    id = this.id,
                    firstName = this.firstName,
                    lastName = this.lastName,
                    resourceUrl = this.resourceUrl,
                    country = this.country,
                    age = this.age
                )
            }
        } else User()
    }
}
