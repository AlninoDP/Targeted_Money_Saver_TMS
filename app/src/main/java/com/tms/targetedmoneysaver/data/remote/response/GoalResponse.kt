package com.tms.targetedmoneysaver.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GoalResponse(
    val error: Boolean,
    val message: String,
    val listGoal: List<GoalItem>
)

@Parcelize
data class GoalItem(
    val goal: Goal,
    val product: GoalProduct,
    val tracker: GoalTracker
) : Parcelable

@Parcelize
data class Goal(
    val image: String,
    val name: String,
    val daysPeriod: Int,
    val dateStarted: String,
) : Parcelable

@Parcelize
data class GoalProduct(
    val price: Int,
    val title: String
) : Parcelable

@Parcelize
data class GoalTracker(
    val daysRemaining: Int,
    val daysSaved: Int,
    val moneySaved: Int,
    val dailySaving: Int,
) : Parcelable