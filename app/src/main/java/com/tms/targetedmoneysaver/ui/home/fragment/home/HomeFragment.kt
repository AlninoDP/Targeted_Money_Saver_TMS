package com.tms.targetedmoneysaver.ui.home.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.data.local.entity.GoalEntity
import com.tms.targetedmoneysaver.databinding.FragmentHomeBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import es.dmoral.toasty.Toasty

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val homeViewModel by viewModels<HomeViewModel> {
            factory
        }

        homeViewModel.getAllGoals().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(result.state)
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            showEmptyState()
                        } else {
                            homeViewModel.getClosestGoal().observe(viewLifecycleOwner) {
                                setUpClosestGoal(it)
                            }
                        }
                    }

                    is Result.Failure -> {
                        Toasty.error(requireContext(), "Something went wrong", Toasty.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }

        return binding.root
    }

    private fun setUpClosestGoal(goalEntity: GoalEntity) {
        binding.apply {
            closestGoalTitle.text = goalEntity.title
            homeTotalDaysSaved.text =
                getString(R.string.total_days_saved, goalEntity.daysSaved)
            homeTotalMoneySaved.text =
                getString(R.string.total_money_saved, goalEntity.amountSaved)
            homeRemainingDays.text =
                getString(R.string.remaining_days, goalEntity.daysRemaining)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.homeProgressBar1.visibility = View.VISIBLE
            binding.closestGoalItem.visibility = View.GONE
            binding.closestGoalImageDetail.visibility = View.GONE
        } else {
            binding.homeProgressBar1.visibility = View.GONE
            binding.closestGoalItem.visibility = View.VISIBLE
            binding.closestGoalImageDetail.visibility = View.VISIBLE
        }
    }

    private fun showEmptyState() {
        binding.apply {
            emptyGoalData.root.visibility = View.VISIBLE
            closestGoalItem.visibility = View.GONE
            closestGoalImageDetail.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}