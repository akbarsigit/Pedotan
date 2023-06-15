package com.capstone.pedotan.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.pedotan.di.Injection
import com.capstone.pedotan.model.request.AddFieldRequest
import com.capstone.pedotan.ui.addfield.AddFieldActivityViewModel
import com.capstone.pedotan.ui.camera.CameraActivityViewModel
import com.capstone.pedotan.ui.checkfield.CheckFieldViewModel
import com.capstone.pedotan.ui.contract.ContractViewModel
import com.capstone.pedotan.ui.dashboard.DashboardViewModel
import com.capstone.pedotan.ui.history.HistoryViewModel
import com.capstone.pedotan.ui.loan.LoanViewModel
import com.capstone.pedotan.ui.login.LoginActivityViewModel
import com.capstone.pedotan.ui.market.MarketViewModel
import com.capstone.pedotan.ui.profile.ProfileViewModel
import com.capstone.pedotan.ui.register.RegisterActivityViewModel
import com.capstone.pedotan.ui.setting.SettingViewModel
import com.capstone.pedotan.ui.updateprofile.UpdateProfileViewModel

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
        else if (modelClass.isAssignableFrom(LoanViewModel::class.java)) {
            return LoanViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(ContractViewModel::class.java)) {
            return ContractViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(MarketViewModel::class.java)) {
            return MarketViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)) {
            return RegisterActivityViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(LoginActivityViewModel::class.java)) {
            return LoginActivityViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(UpdateProfileViewModel::class.java)) {
            return UpdateProfileViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(AddFieldActivityViewModel::class.java)) {
            return AddFieldActivityViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(CheckFieldViewModel::class.java)) {
            return CheckFieldViewModel(Injection.provideRepository(context)) as T
        }
        else if (modelClass.isAssignableFrom(CameraActivityViewModel::class.java)) {
            return CameraActivityViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}