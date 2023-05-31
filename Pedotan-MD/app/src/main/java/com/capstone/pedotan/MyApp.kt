package com.capstone.pedotan

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.capstone.pedotan.data.SettingsRepository

class MyApp : Application() {
    private lateinit var repository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        repository = SettingsRepository(this)
        val settings = repository.getSettings()

        when (settings.isDarkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}
