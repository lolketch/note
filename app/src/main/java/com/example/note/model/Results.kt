package com.example.note.model
import com.google.gson.annotations.SerializedName

data class Results (

	@SerializedName("gender") val gender : String,
	@SerializedName("name") val name : Name,
	@SerializedName("email") val email : String,
	@SerializedName("phone") val phone : String,
	@SerializedName("picture") val picture : Picture,
)