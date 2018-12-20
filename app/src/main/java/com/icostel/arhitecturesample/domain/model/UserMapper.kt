package com.icostel.arhitecturesample.domain.model

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Used for mapping from api/db to domain and from domain to view models
 */

@Singleton
class UserMapper @Inject constructor() {
    fun mapApiToDomain(dbUser: com.icostel.arhitecturesample.api.model.User): User {
        return User(dbUser.id, dbUser.firstName, dbUser.lastName, dbUser.resourceUrl, dbUser.country, dbUser.age)
    }

    fun mapDomainToApi(domainUser: User): com.icostel.arhitecturesample.api.model.User {
        return com.icostel.arhitecturesample.api.model.User(domainUser.id, domainUser.firstName, domainUser.lastName, domainUser.resourceUrl, domainUser.country, domainUser.age)
    }

    fun mapDomainToApi(domainUsers: List<User>?): List<com.icostel.arhitecturesample.api.model.User> {
        val dbUsers = ArrayList<com.icostel.arhitecturesample.api.model.User>()
        domainUsers?.forEach {
            dbUsers.add(mapDomainToApi(it))
        }
        return dbUsers
    }

    fun mapApiToDomain(apiUsers: List<com.icostel.arhitecturesample.api.model.User>?): List<User> {
        val domainUsers = ArrayList<User>()
        apiUsers?.forEach {
            domainUsers.add(mapApiToDomain(it))
        }
        return domainUsers
    }
}
