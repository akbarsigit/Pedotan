package com.capstone.pedotan.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("fullname")
    val fullname: String,

    @field:SerializedName("password")
    val password: String
): Parcelable