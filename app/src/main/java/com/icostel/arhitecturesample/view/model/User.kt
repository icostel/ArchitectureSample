package com.icostel.arhitecturesample.view.model

/** The View view model */
data class User constructor(var id: String = "", var firstName: String = "", var lastName: String = "", var resourceUrl: String = "", var country: String = "", var age: Int = 0) {
    override fun toString(): String {
        return "$TAG(id=$id, firstName='$firstName', lastName='$lastName', resourceUrl='$resourceUrl', country='$country')"
    }

    companion object {
        const val TAG = "User"
    }
}