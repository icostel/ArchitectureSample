package com.icostel.arhitecturesample.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.icostel.arhitecturesample.Config

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
        var age: Int) {

    companion object Consts {
        val UNDEFINED = ""
        val NO_AGE = -1
    }

    @Ignore constructor() : this(UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED, NO_AGE)

    override fun toString(): String {
        return "User(id='$id', firstName='$firstName', lastName='$lastName', resourceUrl='$resourceUrl', country='$country', age=$age)"
    }
}