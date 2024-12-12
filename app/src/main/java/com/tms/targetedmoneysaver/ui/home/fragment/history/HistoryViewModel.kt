package com.tms.targetedmoneysaver.ui.home.fragment.history

import androidx.lifecycle.ViewModel
import com.tms.targetedmoneysaver.data.MainRepository

class HistoryViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun getCompletedGoals()  = mainRepository.getCompletedGoals()
}