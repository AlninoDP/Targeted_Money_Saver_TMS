package com.tms.targetedmoneysaver.ui.add_goal

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddGoalViewModel : ViewModel() {

    // Goal Period
    private val _periodSliderValue = MutableLiveData<Float>()
    val periodSliderValue: LiveData<Float> get() = _periodSliderValue

    // Goal Item Image
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    // Goal Item Name
    private val _goalName = MutableLiveData<String>()
    val goalName: LiveData<String> get() = _goalName

    // Goal Item Price
    private val _goalPrice = MutableLiveData<String>()
    val goalPrice: LiveData<String> get() = _goalPrice



    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun getImageUri(): Uri? = _imageUri.value

    fun setPeriodSliderValue(value: Float){
        _periodSliderValue.value = value
    }

    fun addPeriodSliderValue(value : Float) {
        val currentValue = _periodSliderValue.value ?: 0f
        _periodSliderValue.value = minOf(currentValue + value, 365f)
    }


}