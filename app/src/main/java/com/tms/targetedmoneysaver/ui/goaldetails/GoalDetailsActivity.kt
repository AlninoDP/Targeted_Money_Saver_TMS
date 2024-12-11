package com.tms.targetedmoneysaver.ui.goaldetails

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity
import com.tms.targetedmoneysaver.databinding.ActivityGoalDetailsBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.home.HomeActivity
import com.tms.targetedmoneysaver.utils.addDaysToDate
import es.dmoral.toasty.Toasty

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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@GoalDetailsActivity)
        val goalDetailViewModel by viewModels<GoalDetailsViewModel> {
            factory
        }

        setUpAppbar()

        val goalEntity = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_GOAL_ITEM, GoalEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_GOAL_ITEM)
        }

        goalEntity?.let {
            setGoalDetail(it)

            if (it.daysRemaining == 0){
                binding.goalDetailSaveTodayButton.isEnabled = false
                binding.goalDetailSaveTodayButton.text = getString(R.string.goal_detail_goal_completed_text)
            }
        }

        binding.goalDetailSaveTodayButton.setOnClickListener {
            goalDetailViewModel.updateSaving(goalEntity?.id ?: "")
                .observe(this@GoalDetailsActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoading(result.state)
                            is Result.Success -> {
                                val message = result.data.message
                                Toasty.success(
                                    this@GoalDetailsActivity,
                                    message,
                                    Toast.LENGTH_SHORT,
                                    true
                                ).show()

                               val intent = Intent(this@GoalDetailsActivity, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()
                            }

                            is Result.Failure -> {
                                Toasty.error(
                                    this@GoalDetailsActivity,
                                    result.throwable.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                }
        }


    }

    private fun setGoalDetail(goal: GoalEntity) {
        binding.apply {
            // TODO: SET IMAGE
            with(goal) {
                Glide.with(this@GoalDetailsActivity)
                    .load(image)
                    .into(goalDetailImage)

                goalDetailDaysProgressBar.progressMax = period.toFloat()
                goalDetailDaysProgressBar.progress = daysSaved.toFloat()

                goalDetailArcProgressText.text =
                    getString(R.string.goal_detail_progress_text, daysSaved, period)
                goalDetailDaysSaved.text =
                    getString(R.string.goal_detail_total_days_saved, daysSaved)
                goalDetailMoneySaved.text =
                    getString(R.string.goal_detail_total_money_saved, amountSaved)
                goalDetailRemainingDays.text =
                    getString(R.string.goal_detail_remaining_days, daysRemaining)
                goalDetailDailySaving.text =
                    getString(R.string.goal_detail_daily_saving_amount, dailySave)
                goalDetailCategory.text = category

                goalDetailTitle.text = title
                goalDetailDescription.text = description
                goalDetailAmount.text = getString(R.string.goal_detail_item_price, amount)
                goalDetailPeriod.text = getString(R.string.goal_breakdown_period, period)
                goalDetailItemDateStarted.text = dateStarted
                goalDetailItemDateCompletion.text =
                    addDaysToDate(dateStarted, daysRemaining.toLong())
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.goalDetailProgressBar.visibility = View.VISIBLE
            binding.goalDetailSaveTodayButton.visibility = View.GONE
        } else {
            binding.goalDetailProgressBar.visibility = View.GONE
            binding.goalDetailSaveTodayButton.visibility = View.VISIBLE
        }
    }

    private fun setUpAppbar() {
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