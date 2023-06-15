package com.capstone.pedotan.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FieldResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("farm_id")
	val farm_id: String,

	@field:SerializedName("area")
	val area: String,

	@field:SerializedName("commodity")
	val commodity: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable
