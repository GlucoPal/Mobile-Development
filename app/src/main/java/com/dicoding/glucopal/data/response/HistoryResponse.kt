package com.dicoding.glucopal.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<HistoryItem?>? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

data class HistoryItem(

	@field:SerializedName("food_name")
	val foodName: String? = null,

	@field:SerializedName("GI")
	val gI: Float? = null,

	@field:SerializedName("GL")
	val gL: Float? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
