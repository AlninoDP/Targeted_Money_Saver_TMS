package com.tms.targetedmoneysaver.ui.addgoal.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalImageBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalViewModel
import com.tms.targetedmoneysaver.utils.getImageUri
import es.dmoral.toasty.Toasty


class AddGoalImageFragment : Fragment() {

    private var _binding: FragmentAddGoalImageBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGoalImageBinding.inflate(inflater, container, false)

        requestPermissionsIfNeeded()
        showImage()
        setUpAppBar()

        binding.searchImageIcon.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Pick Image From")
            alertDialogBuilder.setItems(options) { _, which ->
                when (which) {
                    0 -> startCamera()
                    1 -> startGallery()
                }
            }
            alertDialogBuilder.show()
        }

        binding.addGoalButtonConfirm.setOnClickListener {

            if (isFormValid()) {
                addGoalViewModel.updateTitle(binding.etGoalTitle.text.toString())
                addGoalViewModel.updateAmount(binding.etGoalAmount.text.toString().toInt())
                addGoalViewModel.updateDescription(binding.etGoalDescription.text.toString().trim())

                // TODO: SEND INFORMATION TO ANALYZE AND GET CATEGORY PREDICTION
                addGoalViewModel.goal.value?.imageUri?.let {
                    findNavController().navigate(R.id.action_addGoalImageFragment_to_addGoalPeriodFragment)

                } ?: Toasty.error(requireContext(), "Please select an image", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.addGoalBtnClearImage.setOnClickListener {
            if (addGoalViewModel.goal.value?.imageUri != null) {
                addGoalViewModel.updateImageUri(null)
                binding.imgChosen.setImageResource(R.drawable.placeholder_image)
                binding.searchImageIcon.visibility = View.VISIBLE
                Toasty.info(requireContext(), "Image cleared", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root

    }

    private fun isFormValid(): Boolean {
        val goalTitle = binding.etGoalTitle.text.toString()
        val goalAmount =
            binding.etGoalAmount.text.toString()
        val goalDescription =
            binding.etGoalDescription.text.toString()

        var isValid = true

        if (goalTitle.isEmpty()) {
            binding.etGoalTitle.error = "Goal title is required"
            isValid = false
        } else {
            binding.etGoalTitle.error = null
        }

        if (goalAmount.isEmpty()) {
            binding.etGoalAmount.error = "Goal amount is required"
            isValid = false
        } else {
            binding.etGoalAmount.error = null
        }

        if (goalDescription.isEmpty()) {
            binding.etGoalDescription.error = "Goal description is required"
            isValid = false
        } else {
            binding.etGoalDescription.error = null
        }

        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // Camera
    private fun startCamera() {
        addGoalViewModel.updateImageUri(getImageUri(requireContext()))
        addGoalViewModel.goal.value?.imageUri?.let { launcherIntentCamera.launch(it) } ?: {
            Toasty.error(requireContext(), "Uh Oh Something Went Wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            // Reset to placeholder if no image is captured
            addGoalViewModel.updateImageUri(null)
            binding.imgChosen.setImageResource(R.drawable.placeholder_image)
            binding.searchImageIcon.visibility = View.VISIBLE
        }
    }

    // Gallery
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                addGoalViewModel.updateImageUri(uri)
            }
        }

    private fun showImage() {
        addGoalViewModel.goal.observe(viewLifecycleOwner) { goal ->
            goal?.imageUri?.let {
                binding.imgChosen.setImageURI(it)
                binding.searchImageIcon.visibility = View.GONE
            }
        }

    }

    /// Request Permission
    private fun requestPermissionsIfNeeded() {
        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            val message = if (isGranted) "Permission Granted" else "Permission Denied"
            Toasty.info(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}