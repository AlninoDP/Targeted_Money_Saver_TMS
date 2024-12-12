package com.tms.targetedmoneysaver.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateSavingResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("amount_saved")
	val amountSaved: Int,

	@field:SerializedName("daily_save")
	val dailySave: Int,

	@field:SerializedName("days_saved")
	val daysSaved: Int,

	@field:SerializedName("days_remaining")
	val daysRemaining: Int
)
