package com.tms.targetedmoneysaver.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.MainRepository
import com.tms.targetedmoneysaver.data.di.Injection
import com.tms.targetedmoneysaver.data.local.UserPreferences
import com.tms.targetedmoneysaver.data.local.datastore
import com.tms.targetedmoneysaver.ui.login.LoginViewModel

class ViewModelFactory private constructor(
    private val mainRepository: MainRepository,
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val mainRepository = Injection.provideRepository(context)
                val authRepository = Injection.provideAuthRepository(context)
                val preferences = UserPreferences.getInstance(context.datastore)
                instance ?: ViewModelFactory(mainRepository, authRepository, preferences)
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                authRepository,
                userPreferences
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
