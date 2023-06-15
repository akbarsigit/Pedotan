package com.capstone.pedotan.model.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
	@field:SerializedName("message")
	val message: String,
)

data class PredictResponse(
	@field:SerializedName("predict")
	val predict: String,
)

data class NPKResponse(
	@field:SerializedName("n")
	val n: Float,

	@field:SerializedName("p")
	val p: Float,

	@field:SerializedName("k")
	val k: Float,
)

data class DiseaseResponse(
	@field:SerializedName("predict")
	val predict: String,
)