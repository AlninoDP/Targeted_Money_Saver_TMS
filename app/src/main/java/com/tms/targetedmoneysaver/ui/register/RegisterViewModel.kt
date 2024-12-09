package com.tms.targetedmoneysaver.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.data.local.UserPreferences

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
): ViewModel() {

    private val _registerResult = MutableLiveData< Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult


    fun registerResult(email:String, password: String){
        _registerResult.value = Result.Loading(true)
    }

}