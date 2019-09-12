package com.icostel.arhitecturesample.api.session

import javax.inject.Singleton

//TODO add more user information
@Singleton
data class SessionData
constructor(internal var userToken: String? = null)
