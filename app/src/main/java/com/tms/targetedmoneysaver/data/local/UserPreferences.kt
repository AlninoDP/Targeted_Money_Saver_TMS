package com.tms.targetedmoneysaver.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(
    private val datastore: DataStore<Preferences>
) {


    fun getUserToken(): Flow<String> {
        return datastore.data.map { preferences ->
            preferences[ID_TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveUserToken(token: String) {
        datastore.edit { preferences ->
            preferences[ID_TOKEN_KEY] = token
        }
    }

    suspend fun deleteUserToken() {
        datastore.edit { preferences ->
            preferences.remove(ID_TOKEN_KEY)
        }
    }

    fun getDailyNotificationSetting() :Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[DAILY_NOTIFICATION_KEY] ?: false
        }
    }

    suspend fun saveDailyNotificationSetting(isDailyNotificationActive: Boolean) {
        datastore.edit { preferences ->
            preferences[DAILY_NOTIFICATION_KEY] = isDailyNotificationActive
        }
    }

    companion object {

        private val ID_TOKEN_KEY = stringPreferencesKey("id_token")
        private val DAILY_NOTIFICATION_KEY = booleanPreferencesKey("notification_settings")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            //synchronized used so all thread are sync and only one thread that can run this
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}