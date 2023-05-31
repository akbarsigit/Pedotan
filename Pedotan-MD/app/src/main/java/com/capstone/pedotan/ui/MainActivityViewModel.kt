package com.capstone.pedotan.ui

import androidx.lifecycle.ViewModel
import com.capstone.pedotan.data.SettingsRepository
import com.senpro.ulamsae.model.Settings

class MainActivityViewModel(private val repository: SettingsRepository) : ViewModel() {

    fun getCurrentSettings(): Settings {
        return repository.getSettings()
    }
}