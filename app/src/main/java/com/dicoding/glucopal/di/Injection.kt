package com.dicoding.glucopal.di

import android.content.Context
import android.util.Log
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.pref.UserPreference
import com.dicoding.glucopal.data.pref.dataStore
import com.dicoding.glucopal.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {

        val pref = UserPreference.getInstance(context.dataStore)
        Log.d("ILHAN - Injection", "pref: $pref")
        val user = pref.getSession()
        Log.d("ILHAN - Injection", "user: $user")
        val apiService = ApiConfig.getApiService(user)

        return Repository.getInstance(apiService, pref)
    }
}
