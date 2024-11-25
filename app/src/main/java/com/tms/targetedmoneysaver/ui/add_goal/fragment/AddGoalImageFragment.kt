package com.tms.targetedmoneysaver.ui.add_goal.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.databinding.FragmentAddGoalImageBinding
import com.tms.targetedmoneysaver.ui.add_goal.AddGoalViewModel
import com.tms.targetedmoneysaver.utils.getImageUri


class AddGoalImageFragment : Fragment() {

    private var _binding: FragmentAddGoalImageBinding? = null
    private val binding get() = _binding!!

    private val addGoalViewModel: AddGoalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGoalImageBinding.inflate(inflater, container, false)

        requestPermissionsIfNeeded()
        showImage()

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
            // TODO: SEND IMAGE TO ANALYZE AND GET PREDICTION
            findNavController().navigate(R.id.action_addGoalImageFragment_to_addGoalPeriodFragment)
        }

        binding.addGoalBtnClearImage.setOnClickListener {
            addGoalViewModel.setImageUri(null)
            binding.imgChosen.setImageResource(R.drawable.placeholder_image)
            binding.searchImageIcon.visibility = View.VISIBLE
        }
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // Camera
    private fun startCamera() {
        addGoalViewModel.setImageUri(getImageUri(requireContext()))
        launcherIntentCamera.launch(addGoalViewModel.getImageUri())
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    // Gallery
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                addGoalViewModel.setImageUri(uri)
            } else {
                Log.d("Photo Picker", "No Media Selected")
            }
        }

    private fun showImage() {
        addGoalViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                binding.imgChosen.setImageURI(it)
                binding.searchImageIcon.visibility = View.GONE
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
            showToast(message)
        }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}