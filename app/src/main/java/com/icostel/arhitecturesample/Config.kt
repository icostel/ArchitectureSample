package com.icostel.arhitecturesample

interface Config {

    companion object {
        const val REST_ENDPOINT = BuildConfig.HOST_NAME
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
