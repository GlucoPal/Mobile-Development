package com.dicoding.glucopal.data.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("data")
	val data: List<DetailItem?>? = null,

	@field:SerializedName("success")
	val success: Int? = null
)

data class DetailItem(

	@field:SerializedName("food_name")
	val foodName: String? = null,

	@field:SerializedName("GI")
	val gI: Float? = null,

	@field:SerializedName("charbo")
	val charbo: String? = null,

	@field:SerializedName("GL")
	val gL: Float? = null,

	@field:SerializedName("image_path")
	val imagePath: String? = null,

	@field:SerializedName("protein")
	val protein: String? = null,

	@field:SerializedName("serving_size")
	val servingSize: String? = null,

	@field:SerializedName("fat")
	val fat: String? = null,

	@field:SerializedName("calorie")
	val calorie: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_food")
	val idFood: Int? = null
)
