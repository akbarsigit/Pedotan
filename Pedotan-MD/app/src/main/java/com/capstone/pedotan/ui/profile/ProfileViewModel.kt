package com.capstone.pedotan.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.response.User
import com.capstone.pedotan.model.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val repository: SettingsRepository) : ViewModel() {

    private val _user = MutableLiveData<User?>(null)
    val user : LiveData<User?> get() = _user

    fun getSettings() {
        val settings = repository.getSettings()
        setUserDetail(settings.userID)
    }

    private fun setUserDetail(username: String) {
        val client = ApiConfig().getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _user.value = response.body()?.data?.user
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