package com.tms.targetedmoneysaver.ui.home.fragment.setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.tms.targetedmoneysaver.GoalWorker
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.data.Result.Failure
import com.tms.targetedmoneysaver.data.Result.Loading
import com.tms.targetedmoneysaver.data.Result.Success
import com.tms.targetedmoneysaver.databinding.FragmentSettingBinding
import com.tms.targetedmoneysaver.ui.ViewModelFactory
import com.tms.targetedmoneysaver.ui.aboutapp.AboutAppActivity
import com.tms.targetedmoneysaver.ui.login.LoginActivity
import es.dmoral.toasty.Toasty
import java.util.Locale
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        workManager = WorkManager.getInstance(requireContext())
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val settingViewModel by viewModels<SettingViewModel> {
            factory
        }

        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        settingViewModel.signOutResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Loading -> {
                    showLoading(result.state)
                }

                is Success -> {
                    val message = result.data
                    Toasty.success(requireContext(), message, Toast.LENGTH_SHORT, true).show()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    requireActivity().finish()
                }

                is Failure -> {
                    Toasty.error(
                        requireContext(),
                        result.throwable.message.toString(),
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
            }
        }


        settingViewModel.getDailyNotificationSetting()
            .observe(viewLifecycleOwner) { isDailyNotificationActive ->
                Log.d("AASD", "LIGLOG $isDailyNotificationActive")
                binding.swNotification.isChecked = isDailyNotificationActive

                if (isDailyNotificationActive) {
                    sentNotificationPeriodically()
                }
            }

        binding.swNotification.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    sentNotificationPeriodically()
                    settingViewModel.saveDailyNotificationSetting(true)
                }

                false -> {
                    cancelPeriodTask()
                    settingViewModel.saveDailyNotificationSetting(false)
                }
            }
        }

        val currentLanguage = Locale.getDefault().displayLanguage
        binding.tvLanguageSetting.text = getString(R.string.setting_language_text, currentLanguage)
        binding.tvLanguageSetting.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.tvAboutApp.setOnClickListener {
            val intent = Intent(requireContext(), AboutAppActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignOut.setOnClickListener {
            settingViewModel.signOut()
        }

        return binding.root
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toasty.success(requireContext(), "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toasty.error(requireContext(), "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun sentNotificationPeriodically() {
        val constraint = Constraints.Builder().build()

        periodicWorkRequest =
            PeriodicWorkRequest.Builder(GoalWorker::class.java, 15, TimeUnit.DAYS)
                .setConstraints(constraint)
                .build()

        workManager.enqueueUniquePeriodicWork(
            "GoalNotificationWork",
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if already enqueued
            periodicWorkRequest
        )
    }

    private fun cancelPeriodTask() {
        workManager.cancelUniqueWork("GoalNotificationWork")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.settingProgressBar.visibility = View.VISIBLE
            binding.btnSignOut.visibility = View.GONE
        } else {
            binding.settingProgressBar.visibility = View.GONE
            binding.btnSignOut.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}