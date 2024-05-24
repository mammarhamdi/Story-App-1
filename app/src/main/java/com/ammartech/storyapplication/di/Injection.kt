package com.ammartech.storyapplication.di

import android.content.Context
import com.ammartech.storyapplication.data.repository.UserRepository
import com.ammartech.storyapplication.data.pref.UserPreference
import com.ammartech.storyapplication.data.pref.dataStore
import com.ammartech.storyapplication.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val userPref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(userPref, apiService)
    }
}
