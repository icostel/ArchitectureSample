package com.icostel.arhitecturesample.api.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.icostel.arhitecturesample.Config
import timber.log.Timber

/**
 * The data object used by rest and room apis, this will be read by a mock REST server and stored in DB
 */

@Entity(tableName = Config.Db.USER_TABLE)
data class User(
        @PrimaryKey
        @NonNull
        @SerializedName("id")
        @ColumnInfo(name = "id")
        var id: String,

        @SerializedName("first_name")
        @ColumnInfo(name = "first_name")
        var firstName: String,

        @SerializedName("last_name")
        @ColumnInfo(name = "last_name")
        var lastName: String,

        @SerializedName("resource_url")
        @ColumnInfo(name = "resource_url")
        var resourceUrl: String,

        @SerializedName("country")
        @ColumnInfo(name = "country")
        var country: String,

        @SerializedName("age")
        @ColumnInfo(name = "age")
        var age: String) {

    companion object Consts {
        val TAG: String? = User::class.java.canonicalName
        const val UNDEFINED = ""
    }

    @Ignore constructor() : this(UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED)

    override fun toString(): String {
        return "User(id='$id', firstName='$firstName', lastName='$lastName', resourceUrl='$resourceUrl', country='$country', age=$age)"
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is User) {
            Timber.w("%s (other) is not of type User, ignoring...", TAG)
            false
        } else {
            this.id == other.id
                    && this.age == other.age
                    && this.country == other.country
                    && this.firstName == other.firstName
                    && this.lastName == other.lastName
                    && this.resourceUrl == other.resourceUrl
        }
    }
}