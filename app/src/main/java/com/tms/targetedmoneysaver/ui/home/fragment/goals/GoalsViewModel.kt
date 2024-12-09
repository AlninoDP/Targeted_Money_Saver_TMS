package com.tms.targetedmoneysaver.ui.home.fragment.goals

import androidx.lifecycle.ViewModel
import com.tms.targetedmoneysaver.data.MainRepository

class GoalsViewModel(private val mainRepository: MainRepository): ViewModel() {

        fun getAllGoals() = mainRepository.getAllGoals()

        fun getMostSavedGoals() = mainRepository.getMostSavedGoals()

}