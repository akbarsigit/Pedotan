package com.capstone.pedotan.model.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: UserData,

	@field:SerializedName("status")
	val status: String
)

data class UserData(

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("username")
	val username: String
)
