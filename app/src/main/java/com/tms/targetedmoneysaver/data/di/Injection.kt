package com.tms.targetedmoneysaver.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.tms.targetedmoneysaver.data.AuthRepository
import com.tms.targetedmoneysaver.data.MainRepository
import com.tms.targetedmoneysaver.data.local.datastore
import com.tms.targetedmoneysaver.data.local.room.GoalDatabase
import com.tms.targetedmoneysaver.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context:Context): MainRepository {
        val dataStore = context.datastore
        val apiService = ApiConfig.getApiService(dataStore)
        val database = GoalDatabase.getInstance(context)
        val dao = database.goalDao()
        return MainRepository(apiService, dao)
    }

    fun provideAuthRepository( ): AuthRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        return AuthRepository(firebaseAuth)
    }
}