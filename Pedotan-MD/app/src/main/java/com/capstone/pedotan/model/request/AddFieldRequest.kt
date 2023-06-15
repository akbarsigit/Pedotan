package com.capstone.pedotan.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddFieldRequest(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("commodity")
    val commodity: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("area")
    val area: String
) : Parcelable