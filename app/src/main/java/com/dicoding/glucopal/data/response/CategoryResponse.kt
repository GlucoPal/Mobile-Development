package com.dicoding.glucopal.data.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = emptyList(),

	@field:SerializedName("success")
	val success: Int? = null
)

data class DataItem(

	@field:SerializedName("GI")
	val gI: Int? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("food")
	val food: String? = null
)
