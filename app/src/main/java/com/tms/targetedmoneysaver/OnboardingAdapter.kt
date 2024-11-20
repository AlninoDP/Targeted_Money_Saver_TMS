package com.tms.targetedmoneysaver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tms.targetedmoneysaver.component.OnboardingPage
import com.tms.targetedmoneysaver.databinding.ItemOnboardingPageBinding

class OnboardingAdapter(private val pages: List<OnboardingPage>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    class OnboardingViewHolder(val binding: ItemOnboardingPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(page: OnboardingPage){
            with(binding){
                ivOnboarding.setImageResource(page.onboardingImage)
                tvOnboarding.text = page.onboardingText
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding =
            ItemOnboardingPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun getItemCount(): Int = pages.size

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = pages[position]
        holder.bind(item)
    }
}