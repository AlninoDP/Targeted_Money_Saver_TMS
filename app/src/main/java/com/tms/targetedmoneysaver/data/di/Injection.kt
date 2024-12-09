package com.tms.targetedmoneysaver.data.di

import android.content.Context
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.MainRepository
import com.tms.targetedmoneysaver.data.local.datastore
import com.tms.targetedmoneysaver.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context:Context): MainRepository {
        val dataStore = context.datastore
        val apiService = ApiConfig.getApiService(dataStore)
        return MainRepository(apiService)
    }

    fun provideAuthRepository(context:Context): AuthRepository {
        return AuthRepository()
    }
}