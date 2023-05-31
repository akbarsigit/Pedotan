package com.senpro.ulamsae.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Settings(
    @field:SerializedName("isDarkMode")
    var isDarkMode: Int = 2,

    @field:SerializedName("isLogin")
    var isLogin: Boolean = false,

    @field:SerializedName("token")
    var token: String = "0",

    @field:SerializedName("refToken")
    var refToken: String = "0",

    @field:SerializedName("userID")
    var userID: String = "id"
) : Parcelable
