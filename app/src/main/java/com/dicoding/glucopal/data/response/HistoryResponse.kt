package com.dicoding.glucopal.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<History?>? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

data class History(

	@field:SerializedName("food_name")
	val foodName: String? = null,

	@field:SerializedName("GI")
	val gI: String? = null,

	@field:SerializedName("GL")
	val gL: String? = null
)
