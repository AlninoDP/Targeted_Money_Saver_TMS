package com.tms.targetedmoneysaver.ui.addgoal.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalBreakdownBinding
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalViewModel


class AddGoalBreakdownFragment : Fragment() {
    private var _binding: FragmentAddGoalBreakdownBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels()

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
                goalBreakdownTvGoalAmount.text = goal.amount
                goalBreakdownTvDateStarted.text = goal.dateStarted
                goalBreakdownTvGoalCategory.text = goal.category
                goalBreakdownTvDailySaving.text = getString(R.string.goal_breakdown_daily_saving_text, goal.dailySavingAmount)
                goalBreakdownTvGoalPeriod.text = getString(R.string.goal_breakdown_period, goal.period.toInt())
            }

            goal.imageUri?.let {
                binding.goalBreakdownImageChosen.setImageURI(it)
            }
        }


        binding.apply {
            goalBreakdownBtnStartSaving.setOnClickListener {
                // TODO: SAVE THE DATA TO SERVER AND NAVIGATE TO HOME
                findNavController().navigate(R.id.action_addGoalBreakdownFragment_to_homeActivity)
            }
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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