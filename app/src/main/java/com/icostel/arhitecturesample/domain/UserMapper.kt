package com.icostel.arhitecturesample.domain

/**
 * Used for mapping from api/db to domain and from domain to view models
 */

class UserMapper {
    fun mapDbToDomain(dbUser: com.icostel.arhitecturesample.api.model.User): User {
        val domainUser = User(dbUser.id, dbUser.firstName, dbUser.lastName, dbUser.resourceUrl, dbUser.country, dbUser.age)
        return domainUser
    }

    fun mapDomainToDb(domainUser: User): com.icostel.arhitecturesample.api.model.User {
        val dbUser = com.icostel.arhitecturesample.api.model.User(domainUser.id, domainUser.firstName, domainUser.lastName, domainUser.resourceUrl, domainUser.country, domainUser.age)
        return dbUser
    }
}
