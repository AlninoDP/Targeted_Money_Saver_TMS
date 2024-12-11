package com.tms.targetedmoneysaver.ui.home.fragment.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.work.PeriodicWorkRequest
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

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View   {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val settingViewModel by viewModels<SettingViewModel> {
            factory
        }

        settingViewModel.signOutResult.observe(viewLifecycleOwner){result ->
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


        settingViewModel.getDailyNotificationSetting().observe(viewLifecycleOwner){isDailyNotificationActive ->
            binding.swNotification.isChecked = isDailyNotificationActive
            if(isDailyNotificationActive){
                // TODO: TURN ON PERIODIC WORK
            }
        }

        binding.swNotification.setOnCheckedChangeListener{_, isChecked ->
            when(isChecked){
                true -> {
//                    getEventDataPeriodically()
                    settingViewModel.saveDailyNotificationSetting(true)
                }

                false -> {
//                    cancelPeriodTask()
                    settingViewModel.saveDailyNotificationSetting(false)
                }
            }
        }

        val currentLanguage = Locale.getDefault().displayLanguage
        binding.tvLanguageSetting.text = getString(R.string.setting_language_text,currentLanguage)
        binding.tvLanguageSetting.setOnClickListener{
            startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.tvAboutApp.setOnClickListener{
            val intent = Intent(requireContext(), AboutAppActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignOut.setOnClickListener {
            settingViewModel.signOut()
        }

        return binding.root
    }

//    private fun getEventDataPeriodically() {
//        val constraint = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        periodicWorkRequest =
//            PeriodicWorkRequest.Builder(EventWorker::class.java, 1, TimeUnit.DAYS)
//                .setConstraints(constraint)
//                .build()
//
//        workManager.enqueueUniquePeriodicWork(
//            "EventNotificationWork",
//            ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if already enqueued
//            periodicWorkRequest
//        )
//    }
//
//    private fun cancelPeriodTask() {
//        workManager.cancelUniqueWork("EventNotificationWork")
////      workManager.cancelAllWork()
//    }

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