package com.dicoding.glucopal.data.pref

import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.glucopal.data.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor( private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: LoginResult) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.userId ?: ""
            preferences[NAME_KEY] = user.username ?: ""
            preferences[TOKEN_KEY] = user.token ?: ""
        }
    }

    fun getSession(): Flow<LoginResult> {
        return dataStore.data.map { preferences ->
            LoginResult(
                preferences[EMAIL_KEY],
                preferences[NAME_KEY],
                preferences[TOKEN_KEY]
            )

        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
