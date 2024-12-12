package com.tms.targetedmoneysaver.data.remote.requestbody

data class AddGoalBody (
    val goal_image : String,
    val goal_title: String,
    val goal_amount: Int,
    val goal_description: String,
    val goal_category: String,
    val goal_period: Int,
    val goal_date_started: String,
    val daily_save: Int
)