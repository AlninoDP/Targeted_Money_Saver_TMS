package com.tms.targetedmoneysaver.ui.home.fragment.goals

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tms.targetedmoneysaver.adapter.GoalsAdapter
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.databinding.FragmentGoalsBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalActivity
import es.dmoral.toasty.Toasty

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val goalViewModel by viewModels<GoalsViewModel> {
            factory
        }

        val adapter = GoalsAdapter("GoalsFragment")

        goalViewModel.getAllGoals().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(result.state)
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            showEmptyState()
                        } else {
                            goalViewModel.getMostSavedGoals().observe(viewLifecycleOwner) {
                                adapter.submitList(it)
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

        binding.rvGoals.adapter = adapter
        binding.rvGoals.layoutManager =
            LinearLayoutManager(requireContext())



        binding.fabAddNewGoal.setOnClickListener {
            val intent = Intent(requireContext(), AddGoalActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun showEmptyState() {
        binding.emptyGoalData.root.visibility = View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.goalsProgressBar.visibility = View.VISIBLE
            binding.rvGoals.visibility = View.GONE
        } else {
            binding.goalsProgressBar.visibility = View.GONE
            binding.rvGoals.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}