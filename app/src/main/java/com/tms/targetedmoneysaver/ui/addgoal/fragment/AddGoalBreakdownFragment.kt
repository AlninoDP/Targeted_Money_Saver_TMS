package com.tms.targetedmoneysaver.ui.addgoal.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalBreakdownBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalViewModel
import com.tms.targetedmoneysaver.ui.home.HomeActivity
import es.dmoral.toasty.Toasty


class AddGoalBreakdownFragment : Fragment() {
    private var _binding: FragmentAddGoalBreakdownBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGoalBreakdownBinding.inflate(inflater, container, false)
        setUpAppBar()


        addGoalViewModel.goal.observe(viewLifecycleOwner) { goal ->
            binding.apply {
                goalBreakdownTvGoalTitle.text = goal.title
                goalBreakdownTvGoalDescription.text = goal.description
                goalBreakdownTvGoalAmount.text =
                    getString(R.string.goal_detail_item_price, goal.amount)
                goalBreakdownTvDateStarted.text = goal.dateStarted
                goalBreakdownTvGoalCategory.text = goal.category
                goalBreakdownTvDailySaving.text =
                    getString(R.string.goal_breakdown_daily_saving_text, goal.dailySavingAmount)
                goalBreakdownTvGoalPeriod.text =
                    getString(R.string.goal_breakdown_period, goal.period.toInt())


                goalBreakdownBtnStartSaving.setOnClickListener {

                    val base64String = addGoalViewModel.getBase64String(requireContext())
                    addGoalViewModel.addGoal(
                        goalImage = base64String ?: "",
                        goalTitle = goal.title ?: "",
                        goalAmount = goal.amount,
                        goalDescription = goal.description ?: "",
                        goalCategory = goal.category ?: "",
                        goalPeriod = goal.period.toInt(),
                        goalDateStarted = goal.dateStarted ?: "",
                        goalDailySave = goal.dailySavingAmount
                    ).observe(viewLifecycleOwner) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(result.state)
                                }

                                is Result.Success -> {
                                    val message = result.data.message
                                    Toasty.success(
                                        requireContext(),
                                        message,
                                        Toast.LENGTH_SHORT,
                                        true
                                    ).show()
                                    val intent = Intent(requireContext(), HomeActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    requireActivity().finish()
                                }

                                is Result.Failure -> {
                                    Toasty.error(
                                        requireContext(),
                                        result.throwable.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

            }

            goal.imageUri?.let {
                binding.goalBreakdownImageChosen.setImageURI(it)
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.goalBreakdownProgressBar.visibility = View.VISIBLE
            binding.goalBreakdownBtnStartSaving.visibility = View.GONE
        } else {
            binding.goalBreakdownProgressBar.visibility = View.GONE
            binding.goalBreakdownBtnStartSaving.visibility = View.VISIBLE
        }
    }

    private fun setUpAppBar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.topAppBar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
            }
        }
        binding.topAppBar.navigationIcon?.setTint(
            ContextCompat.getColor(requireContext(), R.color.white)
        )
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}