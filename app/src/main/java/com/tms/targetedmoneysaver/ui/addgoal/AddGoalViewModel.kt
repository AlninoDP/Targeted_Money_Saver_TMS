package com.tms.targetedmoneysaver.ui.addgoal

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.targetedmoneysaver.data.Goal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddGoalViewModel : ViewModel() {

    private val _goal = MutableLiveData(Goal())
    val goal: LiveData<Goal> get() = _goal

    fun updateImageUri(uri: Uri?) {
        _goal.value = _goal.value?.copy(imageUri = uri)
    }

    fun updateTitle(title: String) {
        _goal.value = _goal.value?.copy(title = title)
    }

    fun updateAmount(amount: String) {
        _goal.value = _goal.value?.copy(amount = amount)
    }

    fun updateDescription(description: String) {
        _goal.value = _goal.value?.copy(description = description)
    }

    fun updateCategory(category: String) {
        _goal.value = _goal.value?.copy(category = category)
    }

    fun updatePeriod(period: Float) {
        _goal.value = _goal.value?.copy(period = minOf( period, 365f))
    }

    fun updateDailySavingAmount(dailySaving: Int) {
        _goal.value = _goal.value?.copy(dailySavingAmount = dailySaving)
    }

    fun updateDateStarted() {
        _goal.value = _goal.value?.copy(dateStarted = getCurrentDate())
    }

    private fun getCurrentDate(): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(Date())
    }

}