package com.icostel.arhitecturesample

interface Config {

    companion object {
        const val REST_ENDPOINT = BuildConfig.HOST_NAME
    }

    interface Pref {
        companion object {
            const val PREF_USER = "user"
            const val PREF_APP = "app"
        }
    }

    interface Db {
        companion object {
            const val APP_DB = "app.db"
            const val USER_TABLE = "users"
        }
    }

    interface Data {
        companion object {
            const val USER_ID = "user_id"
        }
    }
}
