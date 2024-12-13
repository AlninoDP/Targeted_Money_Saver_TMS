package com.tms.targetedmoneysaver.ui.addgoal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalPeriodBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalViewModel

class AddGoalPeriodFragment : Fragment() {

    private var _binding: FragmentAddGoalPeriodBinding? = null
    private val binding get() = _binding!!
    private var isSliderUpdating = false

    private val addGoalViewModel: AddGoalViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGoalPeriodBinding.inflate(inflater, container, false)
        setUpAppBar()

        addGoalViewModel.goal.observe(viewLifecycleOwner) { goal ->
            val periodValue = goal.period

            binding.apply {
                tvGoalTitle.text = goal.title
                tvGoalAmount.text = getString(R.string.goal_detail_item_price, goal.amount)
                tvGoalDescription.text = goal.description
                tvGoalCategory.text = goal.category
                addGoalPeriodSlider.value = periodValue
                addGoalTvTotalDays.text = getString(R.string.goal_period_total_days, periodValue)
            }

            goal.imageUri?.let {
                binding.addGoalImageChosen.setImageURI(it)
            }
        }

        binding.apply {
            addGoalsBtnAddWeeks.setOnClickListener {
                val currentPeriod = addGoalViewModel.goal.value?.period ?: 1f
                addGoalViewModel.updatePeriod(currentPeriod + 7f)
            }
            addGoalsBtnAddMonth.setOnClickListener {
                val currentPeriod = addGoalViewModel.goal.value?.period ?: 1f
                addGoalViewModel.updatePeriod(currentPeriod + 30f)
            }
            binding.addGoalPeriodSlider.addOnChangeListener { _, value, fromUser ->
                // Only update the ViewModel if the change comes from the user sliding the slider
                if (fromUser && !isSliderUpdating) {
                    isSliderUpdating = true
                    addGoalViewModel.updatePeriod(value)
                    isSliderUpdating = false
                }
            }
            addGoalBtnNextSteps.setOnClickListener {
                addGoalViewModel.updateDateStarted()
                addGoalViewModel.updateDailySavingAmount(calculateDailySavingAmount())
                findNavController().navigate(R.id.action_addGoalPeriodFragment_to_addGoalBreakdownFragment)
            }
        }
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun calculateDailySavingAmount(): Int {
        val goalsPeriod = addGoalViewModel.goal.value?.period ?: 1f
        val goalsAmount = addGoalViewModel.goal.value?.amount?.toFloat() ?: 1f

        val dailySaving = (goalsAmount / goalsPeriod).toInt()
        return dailySaving
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