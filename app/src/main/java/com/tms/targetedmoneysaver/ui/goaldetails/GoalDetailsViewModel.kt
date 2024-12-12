package com.tms.targetedmoneysaver.ui.goaldetails

import androidx.lifecycle.ViewModel
import com.tms.targetedmoneysaver.data.MainRepository

class GoalDetailsViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun updateSaving(id: String) = mainRepository.updateSaving(id)

}