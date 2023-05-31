package com.capstone.pedotan.di

import android.content.Context
import com.capstone.pedotan.data.SettingsRepository

object Injection {
    fun provideRepository(context: Context): SettingsRepository {
//        val apiService = ApiConfig.getApiService(context)
        return SettingsRepository(context)
    }
}