package com.capstone.pedotan.ui.addfield

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.request.AddFieldRequest
import com.capstone.pedotan.model.request.LoginRequest
import com.capstone.pedotan.model.request.RegisterRequest
import com.capstone.pedotan.model.response.FileUploadResponse
import com.capstone.pedotan.model.response.LoginResponse
import com.senpro.ulamsae.model.Settings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFieldActivityViewModel (private val repository: SettingsRepository) : ViewModel() {

    fun getSettings() : Settings {
        return repository.getSettings()
    }
    fun addField(token: String, addFieldRequest: AddFieldRequest): LiveData<Boolean> {
        val loginLiveData = MutableLiveData<Boolean>()
        val client = ApiConfig().getApiService().addField(token, addFieldRequest)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        loginLiveData.value = true
                        return
                    }
                }
                loginLiveData.value = false
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                loginLiveData.value = false
            }
        })
        return loginLiveData
    }
}