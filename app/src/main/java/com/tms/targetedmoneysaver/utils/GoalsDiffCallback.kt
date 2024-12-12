package com.tms.targetedmoneysaver.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity

class GoalsDiffCallback : DiffUtil.ItemCallback<GoalEntity>() {
    override fun areItemsTheSame(oldItem: GoalEntity, newItem: GoalEntity): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: GoalEntity,
        newItem: GoalEntity
    ): Boolean {
        return oldItem == newItem
    }

}