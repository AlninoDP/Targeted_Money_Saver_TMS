package com.tms.targetedmoneysaver.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.data.local.UserPreferences
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult


    fun registerUser(email: String, password: String) {
        _registerResult.value = Result.Loading(true)
        authRepository.registerUser(email, password) {response ->
            if (response.success) {
                viewModelScope.launch {
                    userPreferences.saveUserToken(response.token)
                }
                _registerResult.value = Result.Success(response.message)
            } else {
                _registerResult.value = Result.Failure(Exception(response.message))
            }
            _registerResult.value = Result.Loading(false)
        }
    }

}