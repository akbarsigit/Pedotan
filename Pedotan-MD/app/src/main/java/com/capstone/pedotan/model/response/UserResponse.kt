package com.capstone.pedotan.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UserResponse(

	@field:SerializedName("nik")
	val nik: String,

	@field:SerializedName("noHandphone")
	val noHandphone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("photo")
	val photo: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("email")
	val email: String
) : Parcelable