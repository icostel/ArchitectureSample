package com.icostel.arhitecturesample.ui.model

/**
 * Used for mapping from api/db to domain and from domain to view models
 */

class UserMapper {
    fun mapViewToDomain(viewModel: User): com.icostel.arhitecturesample.domain.model.User {
        val domainUser = com.icostel.arhitecturesample.domain.model.User(viewModel.id, viewModel.firstName, viewModel.lastName, viewModel.resourceUrl, viewModel.country, viewModel.age)
        return domainUser
    }

    fun mapDomainToView(domainUser: com.icostel.arhitecturesample.domain.model.User): User {
        val viewModel = User(domainUser.id, domainUser.firstName, domainUser.lastName, domainUser.resourceUrl, domainUser.country, domainUser.age)
        return viewModel
    }
}
