package com.tms.targetedmoneysaver.ui.addgoal

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddGoalViewModel : ViewModel() {

    // Goal Period
    private val _periodSliderValue = MutableLiveData<Float?>()
    val periodSliderValue: LiveData<Float?> get() = _periodSliderValue

    // Goal Item Image
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    // Goal Item Name
    private val _goalName = MutableLiveData<String>()
    val goalName: LiveData<String> get() = _goalName

    // Goal Item Price
    private val _goalPrice = MutableLiveData<String>()
    val goalPrice: LiveData<String> get() = _goalPrice

    // Goal Date Started
    private val _dateStarted = MutableLiveData<String>()
    val dateStarted: LiveData<String> get() = _dateStarted

    // Goal Date Finished
    private val _dateFinished = MutableLiveData<String>()
    val dateFinished: LiveData<String> get() = _dateFinished

    // Goal Daily Saving Amount
    private val _dailySaving = MutableLiveData<String>()
    val dailySaving: LiveData<String> get() = _dailySaving




    fun updateDatesBasedOnPeriod() {
        val currentPeriod = _periodSliderValue.value ?: 1
        _dateStarted.value = getCurrentDate()
        _dateFinished.value = getDateAfterDays(currentPeriod.toInt())
    }

    private fun getCurrentDate(): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(Date())
    }

    private fun getDateAfterDays(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(calendar.time)
    }

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun setPeriodSliderValue(value: Float){
        _periodSliderValue.value = value
    }

    fun addPeriodSliderValue(value : Float) {
        val currentValue = _periodSliderValue.value ?: 0f
        _periodSliderValue.value = minOf(currentValue + value, 365f)
    }


}