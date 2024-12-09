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
        authRepository.loginUser(email, password) { success, errorMsg, token ->
            if (success) {
                viewModelScope.launch {
                    userPreferences.saveUserToken(token ?: "")
                }
                Log.d("AAS", "gige: $token")
                _loginResult.value = Result.Success("Login Success")
            } else {
                _loginResult.value = Result.Failure(Exception(errorMsg))
            }
            _loginResult.value = Result.Loading(false)
        }
    }



    // TODO: DELETE THIS
    fun getToken() {
        viewModelScope.launch {
            userPreferences.getUserToken().collect {
                Log.d("AAS", "getTokenten: $it")
            }
        }
    }
}