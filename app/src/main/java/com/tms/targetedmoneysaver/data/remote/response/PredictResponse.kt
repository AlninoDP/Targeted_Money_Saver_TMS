package com.tms.targetedmoneysaver.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(
	@field:SerializedName("category")
	val category: String
)
