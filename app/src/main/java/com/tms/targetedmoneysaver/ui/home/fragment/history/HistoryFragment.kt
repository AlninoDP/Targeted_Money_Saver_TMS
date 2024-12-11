package com.tms.targetedmoneysaver.ui.home.fragment.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.adapter.GoalsAdapter
import com.tms.targetedmoneysaver.databinding.FragmentHistoryBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val historyViewModel by viewModels<HistoryViewModel> {
            factory
        }

        val adapter = GoalsAdapter("HistoryFragment")

        historyViewModel.getCompletedGoals().observe(viewLifecycleOwner) { result ->
            if (result.isEmpty()) {
                showEmptyState()
            } else {
                adapter.submitList(result)
            }
        }

        binding.rvGoalHistory.adapter = adapter
        binding.rvGoalHistory.layoutManager =
            LinearLayoutManager(requireContext())

        return binding.root
    }

    private fun showEmptyState() {
        binding.emptyGoalData.root.visibility = View.VISIBLE
        binding.emptyGoalData.tvEmptyGoalText.text = getString(R.string.empty_goal_history_data)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}