package com.icostel.arhitecturesample.api.session

import javax.inject.Singleton

@Singleton
data class SessionData
constructor(
        internal var userToken: String? = null,
        internal var keepLogIn: Boolean = false,
        internal var userEmail: String? = null,
        internal var userPassword: String? = null
)
