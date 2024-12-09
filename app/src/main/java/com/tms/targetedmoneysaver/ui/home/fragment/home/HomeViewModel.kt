package com.tms.targetedmoneysaver.ui.home.fragment.home

import androidx.lifecycle.ViewModel
import com.tms.targetedmoneysaver.data.MainRepository

class HomeViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun getAllGoals() = mainRepository.getAllGoals()

    fun getClosestGoal() = mainRepository.getClosestGoal()
}