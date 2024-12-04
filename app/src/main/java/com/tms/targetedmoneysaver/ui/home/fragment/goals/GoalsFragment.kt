package com.tms.targetedmoneysaver.ui.home.fragment.goals

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tms.targetedmoneysaver.databinding.FragmentGoalsBinding
import com.tms.targetedmoneysaver.ui.add_goal.AddGoalActivity

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)


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