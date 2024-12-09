package com.tms.targetedmoneysaver.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity
import com.tms.targetedmoneysaver.data.remote.response.DataItem

class GoalsDiffCallback : DiffUtil.ItemCallback<GoalEntity>() {
    override fun areItemsTheSame(oldItem: GoalEntity, newItem: GoalEntity): Boolean {
        return oldItem.title == newItem.title
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: GoalEntity,
        newItem: GoalEntity
    ): Boolean {
        return oldItem == newItem
    }

}