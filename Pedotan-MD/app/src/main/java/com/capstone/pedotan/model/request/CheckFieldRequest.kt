package com.capstone.pedotan.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckFieldRequest(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("farm_id")
    val farm_id: String,

    @field:SerializedName("n")
    val n: Float,

    @field:SerializedName("p")
    val p: Float,

    @field:SerializedName("k")
    val k: Float,

    @field:SerializedName("temperature")
    val temperature: Float,

    @field:SerializedName("humidity")
    val humidity: Float,

    @field:SerializedName("ph")
    val ph: Float,

    @field:SerializedName("rainfall")
    val rainfall: Float
) : Parcelable
