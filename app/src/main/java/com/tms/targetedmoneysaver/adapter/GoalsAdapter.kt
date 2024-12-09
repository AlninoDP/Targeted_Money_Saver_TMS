package com.tms.targetedmoneysaver.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity
import com.tms.targetedmoneysaver.databinding.GoalItemBinding
import com.tms.targetedmoneysaver.ui.goaldetails.GoalDetailsActivity
import com.tms.targetedmoneysaver.utils.GoalsDiffCallback

class GoalsAdapter : ListAdapter<GoalEntity, GoalsAdapter.GoalViewHolder>(GoalsDiffCallback()) {

    class GoalViewHolder(val binding: GoalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: GoalEntity){
            val goalPeriod = goal.period
            val daysSaved = goal.daysSaved
            val goalPercentage = (daysSaved.toDouble() / goalPeriod.toDouble()) * 100

            // TODO: SET IMAGE
            binding.itemGoalName.text = goal.title
            binding.goalProgressBar.setProgressPercentage(goalPercentage)

            itemView.setOnClickListener{
                val intent = Intent(itemView.context, GoalDetailsActivity::class.java)
                intent.putExtra(GoalDetailsActivity.EXTRA_GOAL_ITEM, goal)
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
       val binding = GoalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
       val item = getItem(position)
        holder.bind(item)
    }
}