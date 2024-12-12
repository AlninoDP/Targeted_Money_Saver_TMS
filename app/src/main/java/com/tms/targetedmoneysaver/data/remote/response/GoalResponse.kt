package com.tms.targetedmoneysaver.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GoalResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class DataItem(

	@field:SerializedName("goal")
	val goal: Goal,

	@field:SerializedName("tracker")
	val tracker: Tracker,

	@field:SerializedName("id")
	val id: String
) : Parcelable

@Parcelize
data class Goal(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("period")
	val period: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("date_started")
	val dateStarted: String
): Parcelable

@Parcelize
data class Tracker(

	@field:SerializedName("amount_saved")
	val amountSaved: Int,

	@field:SerializedName("daily_save")
	val dailySave: Int,

	@field:SerializedName("days_saved")
	val daysSaved: Int,

	@field:SerializedName("days_remaining")
	val daysRemaining: Int
): Parcelable
