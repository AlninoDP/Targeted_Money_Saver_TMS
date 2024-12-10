package com.tms.targetedmoneysaver.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddGoalResponse(

	@field:SerializedName("imageURL")
	val imageURL: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("message")
	val message: String
)
