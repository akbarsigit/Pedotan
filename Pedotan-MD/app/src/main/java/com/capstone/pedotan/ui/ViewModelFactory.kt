package com.capstone.pedotan.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.di.Injection
import com.capstone.pedotan.ui.dashboard.DashboardViewModel
import com.capstone.pedotan.ui.login.LoginActivityViewModel
import com.capstone.pedotan.ui.profile.ProfileViewModel
import com.capstone.pedotan.ui.register.RegisterActivityViewModel
import com.capstone.pedotan.ui.setting.SettingViewModel

class ViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)) {
            return RegisterActivityViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(LoginActivityViewModel::class.java)) {
            return LoginActivityViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}