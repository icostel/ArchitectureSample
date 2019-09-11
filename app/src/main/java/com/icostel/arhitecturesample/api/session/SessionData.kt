package com.icostel.arhitecturesample.api.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionData
@Inject constructor() {
    // token received after user logins with user & pass, used for making API calls
    internal var userToken: String? = null
}
