package com.icostel.arhitecturesample.ui.model

import android.os.Bundle

/** The View model */
data class User
constructor(var id: String = "",
            var firstName: String = "",
            var lastName: String = "",
            var resourceUrl: String = "",
            var country: String = "",
            var age: String = "",
            var transitionBundle: Bundle? = null)