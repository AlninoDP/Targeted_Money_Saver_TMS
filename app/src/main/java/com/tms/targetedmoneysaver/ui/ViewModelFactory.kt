package com.tms.targetedmoneysaver.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.MainRepository
import com.tms.targetedmoneysaver.data.di.Injection
import com.tms.targetedmoneysaver.data.local.UserPreferences
import com.tms.targetedmoneysaver.data.local.datastore
import com.tms.targetedmoneysaver.ui.addgoal.AddGoalViewModel
import com.tms.targetedmoneysaver.ui.goaldetails.GoalDetailsViewModel
import com.tms.targetedmoneysaver.ui.home.fragment.goals.GoalsViewModel
import com.tms.targetedmoneysaver.ui.home.fragment.history.HistoryViewModel
import com.tms.targetedmoneysaver.ui.home.fragment.home.HomeViewModel
import com.tms.targetedmoneysaver.ui.home.fragment.setting.SettingViewModel
import com.tms.targetedmoneysaver.ui.login.LoginViewModel
import com.tms.targetedmoneysaver.ui.register.RegisterViewModel

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
                val authRepository = Injection.provideAuthRepository()
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

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(
                authRepository,
                userPreferences
            ) as T

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(mainRepository) as T

            modelClass.isAssignableFrom(GoalsViewModel::class.java) -> GoalsViewModel(mainRepository) as T

            modelClass.isAssignableFrom(GoalDetailsViewModel::class.java) -> GoalDetailsViewModel(
                mainRepository
            ) as T

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(
                mainRepository
            ) as T

            modelClass.isAssignableFrom(AddGoalViewModel::class.java) -> AddGoalViewModel(
                mainRepository
            ) as T

            modelClass.isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(
                authRepository, userPreferences
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

