package com.icostel.arhitecturesample.view.model

/** The view model */
data class User constructor(var id: String, var firstName: String, var lastName: String, var resourceUrl: String, var country: String, var age: Int) {
    override fun toString(): String {
        return "User(id=$id, firstName='$firstName', lastName='$lastName', resourceUrl='$resourceUrl', country='$country')"
    }
}