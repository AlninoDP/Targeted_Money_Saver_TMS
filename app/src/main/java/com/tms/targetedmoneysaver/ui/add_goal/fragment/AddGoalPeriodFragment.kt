package com.tms.targetedmoneysaver.ui.add_goal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalPeriodBinding
import com.tms.targetedmoneysaver.ui.add_goal.AddGoalViewModel

class AddGoalPeriodFragment : Fragment() {

    private var _binding: FragmentAddGoalPeriodBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGoalPeriodBinding.inflate(inflater, container, false)

        addGoalViewModel.periodSliderValue.observe(viewLifecycleOwner) {
            binding.addGoalTvTotalDays.text = getString(R.string.add_goal_total_days, it)
        }
        addGoalViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.addGoalImageChosen.setImageURI(it)
            }
        }

//        binding.addGoalImageChosen.setImageURI(addGoalViewModel.getImageUri())

        binding.apply {

            addGoalsBtnAddWeeks.setOnClickListener {
                addPeriodValue(7f)
                binding.addGoalPeriodSlider.value = addGoalViewModel.periodSliderValue.value!!
            }
            addGoalsBtnAddMonth.setOnClickListener {
                addPeriodValue(30f)
                binding.addGoalPeriodSlider.value = addGoalViewModel.periodSliderValue.value!!
            }
            addGoalPeriodSlider.addOnChangeListener { _, value, _ ->
                addGoalViewModel.setPeriodSliderValue(value)
                addGoalViewModel.updateDatesBasedOnPeriod()
            }
            addGoalBtnNextSteps.setOnClickListener {
                // TODO: GET THE ITEM PRICE AND DIVIDE IT BY THE TOTAL DAYS
                addGoalViewModel.getSliderValue()?.let {
                    findNavController().navigate(R.id.action_addGoalPeriodFragment_to_addGoalBreakdownFragment)
                } ?: Toast.makeText(requireContext(), "Set Your Goal Period!", Toast.LENGTH_SHORT)
                    .show()
            }

            addGoalBtnGoBack.setOnClickListener {
                findNavController().navigate(R.id.action_addGoalPeriodFragment_to_addGoalImageFragment)
            }
        }



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun addPeriodValue(value: Float) {
        addGoalViewModel.addPeriodSliderValue(value)
    }

}