package com.tms.targetedmoneysaver.ui.goaldetails

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.remote.response.GoalItem
import com.tms.targetedmoneysaver.databinding.ActivityGoalDetailsBinding
import com.tms.targetedmoneysaver.utils.addDaysToDate

class GoalDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGoalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpAppbar()

        val goalItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_GOAL_ITEM, GoalItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_GOAL_ITEM)
        }

        goalItem?.let {
            setGoalDetail(it)
        }



    }

    private fun setGoalDetail(goal: GoalItem){
        val goalPeriod = goal.goal.daysPeriod
        val daysSaved = goal.tracker.daysSaved
        val remainingDays = goal.tracker.daysRemaining
        val moneySaved = goal.tracker.moneySaved
        val itemName = goal.product.title
        val itemPrice = goal.product.price
        val dateStarted = goal.goal.dateStarted
        val dailySavingAmount = goal.tracker.dailySaving

        binding.apply {
            // TODO: SET IMAGE
            goalDetailDaysProgressBar.progressMax = goalPeriod.toFloat()
            goalDetailDaysProgressBar.progress = daysSaved.toFloat()

            goalDetailArcProgressText.text = getString(R.string.goal_detail_progress_text, daysSaved, goalPeriod)
            goalDetailDaysSaved.text = getString(R.string.goal_detail_total_days_saved, daysSaved)
            goalDetailMoneySaved.text = getString(R.string.goal_detail_total_money_saved, moneySaved)
            goalDetailRemainingDays.text = getString(R.string.goal_detail_remaining_days, remainingDays)

            goalDetailItemName.text = itemName
            goalDetailItemPrice.text = getString(R.string.goal_detail_item_price, itemPrice)
            goalDetailItemDateStarted.text = dateStarted
            goalDetailItemDateCompletion.text = addDaysToDate(dateStarted, remainingDays.toLong())
            goalDetailItemDailySaving.text = getString(R.string.goal_detail_daily_saving_amount, dailySavingAmount)
        }

    }

    private fun setUpAppbar(){
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_GOAL_ITEM = "extra_goal_item"
    }
}