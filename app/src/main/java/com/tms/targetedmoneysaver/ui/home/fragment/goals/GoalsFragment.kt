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
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalActivity

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        val adapter = GoalsAdapter()

        viewModel.getAllGoals().observe(viewLifecycleOwner){ result ->
            when (result){
                is Result.Loading -> {
                    binding.goalsProgressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    val listGoal = result.data
                    binding.goalsProgressBar.visibility = View.GONE
                    adapter.submitList(listGoal)
                    binding.rvGoals.adapter = adapter
                    binding.rvGoals.layoutManager = LinearLayoutManager(requireContext())
                }
                is Result.Failure -> {
                    // TODO: Handle Error
                }
            }
        }


        binding.fabAddNewGoal.setOnClickListener {
            val intent = Intent(requireContext(), AddGoalActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}