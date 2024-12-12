package com.tms.targetedmoneysaver.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.data.local.UserPreferences
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    fun loginUser(email: String, password: String) {
        _loginResult.value = Result.Loading(true)
        authRepository.loginUser(email, password) { response->
            if (response.success) {
                viewModelScope.launch {
                    userPreferences.saveUserToken(response.token)
                }
                Log.d("LoginViewModel", "Token: ${response.token}")
                _loginResult.value = Result.Success(response.message)
            } else {
                _loginResult.value = Result.Failure(Exception(response.message))
            }
            _loginResult.value = Result.Loading(false)
        }
    }



    fun deleteToken() {
        viewModelScope.launch {
            userPreferences.deleteUserToken()
        }
    }
}