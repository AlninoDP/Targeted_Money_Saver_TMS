package com.tms.targetedmoneysaver.ui.addgoal.fragment

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalBreakdownBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalViewModel
import com.tms.targetedmoneysaver.ui.home.HomeActivity
import es.dmoral.toasty.Toasty
import java.io.ByteArrayOutputStream


class AddGoalBreakdownFragment : Fragment() {
    private var _binding: FragmentAddGoalBreakdownBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

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
                goalBreakdownTvGoalAmount.text = goal.amount.toString()
                goalBreakdownTvDateStarted.text = goal.dateStarted
                goalBreakdownTvGoalCategory.text = goal.category
                goalBreakdownTvDailySaving.text = getString(R.string.goal_breakdown_daily_saving_text, goal.dailySavingAmount)
                goalBreakdownTvGoalPeriod.text = getString(R.string.goal_breakdown_period, goal.period.toInt())


                goalBreakdownBtnStartSaving.setOnClickListener {

                   val base64String =  addGoalViewModel.getBase64String(requireContext())
                    Log.d("GILAA", "LESGO $base64String")

                    addGoalViewModel.addGoal(
                        goalImage= base64String ?: "",
                        goalTitle=  goal.title ?: "",
                        goalAmount= goal.amount,
                        goalDescription= goal.description ?: "",
                        goalCategory= goal.category ?: "",
                        goalPeriod= goal.period.toInt(),
                        goalDateStarted= goal.dateStarted ?: "",
                        goalDailySave= goal.dailySavingAmount
                    ).observe(viewLifecycleOwner){result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
//                                    showLoading(result.state)
                                }
                                is Result.Success -> {
                                    val message = result.data.message
                                    Toasty.success(
                                        requireContext(),
                                        message,
                                        Toast.LENGTH_SHORT,
                                        true
                                    ).show()
                                    val intent = Intent(requireContext(), HomeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                is Result.Failure -> {
                                    Toasty.error(
                                        requireContext(),
                                        result.throwable.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

            }

            goal.imageUri?.let {
                binding.goalBreakdownImageChosen.setImageURI(it)
            }
        }





        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun encode(imageUri: Uri): String {
        val input = activity?.getContentResolver()?.openInputStream(imageUri)
        val image = BitmapFactory.decodeStream(input , null, null)

        // Encode image to base64 string
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return imageString
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