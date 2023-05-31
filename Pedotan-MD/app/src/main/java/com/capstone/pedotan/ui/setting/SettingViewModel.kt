package com.capstone.pedotan.ui.setting

import androidx.lifecycle.ViewModel
import com.capstone.pedotan.data.SettingsRepository
import com.senpro.ulamsae.model.Settings

class SettingViewModel(private val repository: SettingsRepository) : ViewModel() {

    fun getCurrentSettings(): Settings {
        return repository.getSettings()
    }

    fun setDarkMode(state: Int) {
        repository.setDarkMode(state)
    }

    fun removeSession() {
        repository.clearSession()
    }
}