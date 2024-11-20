package com.tms.targetedmoneysaver.ui.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.tms.targetedmoneysaver.OnboardingAdapter
import com.tms.targetedmoneysaver.R
import com.tms.targetedmoneysaver.component.OnboardingPage
import com.tms.targetedmoneysaver.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingPages: List<OnboardingPage>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupOnboardingPage()


        binding.btnNext.setOnClickListener {
            with(binding) {
                if (viewPagerOnboarding.currentItem < onboardingPages.size - 1) {
                    viewPagerOnboarding.currentItem += 1
                } else {
                    // TODO: goto Login page
                    finish()
                }
            }
        }

        // Set button text when user in the last page
        binding.viewPagerOnboarding.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text =
                    if (position == onboardingPages.size - 1) "Get Started" else "Next"
            }
        })


    }

    private fun setupOnboardingPage() {
        onboardingPages = listOf(
            OnboardingPage(R.drawable.onboarding_image_1, getString(R.string.onboarding_Text_1)),
            OnboardingPage(R.drawable.onboarding_image_2, getString(R.string.onboarding_Text_2)),
            OnboardingPage(R.drawable.onboarding_image_3, getString(R.string.onboarding_Text_3))
        )

        // Set Adapter
        val adapter = OnboardingAdapter(onboardingPages)
        binding.viewPagerOnboarding.adapter = adapter

        // Attach dots indicator to viewpager
        binding.springDotsIndicator.attachTo(binding.viewPagerOnboarding)

    }
}