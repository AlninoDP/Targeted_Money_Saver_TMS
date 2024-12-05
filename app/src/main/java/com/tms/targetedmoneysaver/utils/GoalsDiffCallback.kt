package com.tms.targetedmoneysaver.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.tms.targetedmoneysaver.data.remote.response.GoalItem

class GoalsDiffCallback : DiffUtil.ItemCallback<GoalItem>() {
    override fun areItemsTheSame(oldItem: GoalItem, newItem: GoalItem): Boolean {
        return oldItem.goal.name == newItem.goal.name
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: GoalItem,
        newItem: GoalItem
    ): Boolean {
        return oldItem == newItem
    }

}