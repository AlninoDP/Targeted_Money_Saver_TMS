package com.tms.targetedmoneysaver.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity
import com.tms.targetedmoneysaver.databinding.GoalItemBinding
import com.tms.targetedmoneysaver.ui.goaldetails.GoalDetailsActivity
import com.tms.targetedmoneysaver.utils.GoalsDiffCallback

class GoalsAdapter(private val fragmentName:String) : ListAdapter<GoalEntity, GoalsAdapter.GoalViewHolder>(GoalsDiffCallback()) {

    class GoalViewHolder(val binding: GoalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: GoalEntity, fragmentName:String){
            val goalPeriod = goal.period
            val daysSaved = goal.daysSaved
            val goalPercentage = (daysSaved.toDouble() / goalPeriod.toDouble()) * 100

            // TODO: SET IMAGE
            Glide.with(itemView.context)
                .load(goal.image)
                .into(binding.itemGoalImage)


            binding.itemGoalName.text = goal.title
            binding.goalProgressBar.setProgressPercentage(goalPercentage)
            if (fragmentName == "HistoryFragment"){
                binding.goalQuote.text = itemView.context.getString(R.string.goal_completed_quote)
            }
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
        holder.bind(item,fragmentName)
    }
}