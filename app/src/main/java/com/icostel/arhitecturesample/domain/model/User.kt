package com.icostel.arhitecturesample.domain.model

/** The domain model */
data class User constructor(var id: String = "",
                            var firstName: String = "",
                            var lastName: String = "",
                            var resourceUrl: String = "",
                            var country: String = "",
                            var age: String = "") {

    override fun toString(): String {
        return "$TAG(id=$id," +
                "firstName='$firstName'," +
                "lastName='$lastName'," +
                "resourceUrl='$resourceUrl'," +
                "country='$country')"
    }

    companion object {
        private val TAG = User::class.java.simpleName
    }
}