package com.icostel.arhitecturesample.domain.model

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

    fun mapApiUsersToDomain(apiUsers: List<com.icostel.arhitecturesample.api.model.User>?): List<User> {
        val domainUsers = ArrayList<User>()
        apiUsers?.forEach { apiUser ->
            domainUsers.add(User(
                    id = apiUser.id,
                    firstName = apiUser.firstName,
                    lastName = apiUser.lastName,
                    resourceUrl = apiUser.resourceUrl,
                    country = apiUser.country
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
                        country = this.country
                )
            }
        } else User()
    }
}
