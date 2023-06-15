package com.capstone.pedotan.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.response.UserResponse
import com.senpro.ulamsae.model.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val repository: SettingsRepository) : ViewModel() {

    private val _user = MutableLiveData<UserResponse?>(null)
    val user : LiveData<UserResponse?> get() = _user

    fun getSettings() {
        val settings = repository.getSettings()
        setUserDetail(settings)
    }

    private fun setUserDetail(settings: Settings) {
        val client = ApiConfig().getApiService().getUserDetail("Bearer ${settings.token}", settings.email)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
//                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
}