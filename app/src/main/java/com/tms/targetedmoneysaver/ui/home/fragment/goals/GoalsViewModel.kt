package com.tms.targetedmoneysaver.ui.home.fragment.goals

import androidx.lifecycle.ViewModel
import com.tms.targetedmoneysaver.data.MainRepository

class GoalsViewModel: ViewModel() {
    private val repository = MainRepository()

    fun getAllGoals() = repository.getAllGoals()
}