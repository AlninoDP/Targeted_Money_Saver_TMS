package com.tms.targetedmoneysaver.ui.add_goal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalPeriodBinding
import com.tms.targetedmoneysaver.ui.add_goal.AddGoalViewModel
import es.dmoral.toasty.Toasty

class AddGoalPeriodFragment : Fragment() {

    private var _binding: FragmentAddGoalPeriodBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGoalPeriodBinding.inflate(inflater, container, false)
        setUpAppBar()

        addGoalViewModel.periodSliderValue.observe(viewLifecycleOwner) {
            binding.addGoalTvTotalDays.text = getString(R.string.goal_period_total_days, it)
        }
        addGoalViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.addGoalImageChosen.setImageURI(it)
            }
        }

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
                addGoalViewModel.periodSliderValue.value?.let {
                    findNavController().navigate(R.id.action_addGoalPeriodFragment_to_addGoalBreakdownFragment)
                } ?: Toasty.error(requireContext(), "Set Your Goal Period!", Toast.LENGTH_SHORT)
                    .show()

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


    private fun addPeriodValue(value: Float) {
        addGoalViewModel.addPeriodSliderValue(value)
    }

}