package com.tms.targetedmoneysaver.ui.add_goal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalBreakdownBinding
import com.tms.targetedmoneysaver.ui.add_goal.AddGoalViewModel


class AddGoalBreakdownFragment : Fragment() {
    private var _binding: FragmentAddGoalBreakdownBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGoalBreakdownBinding.inflate(inflater, container, false)

        binding.goalBreakdownImageChosen.setImageURI(addGoalViewModel.getImageUri())

        addGoalViewModel.dateStarted.observe(requireActivity()){
            binding.goalBreakdownTvDateStarted.text = it
        }
        addGoalViewModel.dateFinished.observe(requireActivity()){
            binding.goalBreakdownTvDateFinish.text = it
        }


        binding.apply {
            goalBreakdownBtnGoBack.setOnClickListener {
                findNavController().navigate(R.id.action_addGoalBreakdownFragment_to_addGoalPeriodFragment)
            }
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
}