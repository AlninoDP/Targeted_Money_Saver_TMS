package com.tms.targetedmoneysaver.ui.home.fragment.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.Result
import com.tms.targetedmoneysaver.data.local.UserPreferences
import kotlinx.coroutines.launch

class SettingViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    private val _signOutResult = MutableLiveData<Result<String>>()
    val signOutResult: LiveData<Result<String>> get() = _signOutResult

    fun signOut() {
        _signOutResult.value = Result.Loading(true)
        authRepository.signOut { isSuccess ->
            if (isSuccess) {
                _signOutResult.value = Result.Success("Sign Out Success")
            } else {
                _signOutResult.value = Result.Failure(Exception("Error Sign in Out"))
            }
            _signOutResult.value = Result.Loading(false)

        }
    }

    fun getDailyNotificationSetting(): LiveData<Boolean> {
        return userPreferences.getDailyNotificationSetting().asLiveData()
    }

    fun saveDailyNotificationSetting(isDailyNotificationActive: Boolean) {
        viewModelScope.launch {
            userPreferences.saveDailyNotificationSetting(isDailyNotificationActive)
        }
    }

}