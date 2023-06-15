package com.capstone.pedotan.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.request.RegisterRequest
import com.capstone.pedotan.model.response.FileUploadResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivityViewModel (private val repository: SettingsRepository) : ViewModel() {

    fun register(registerRequest: RegisterRequest): LiveData<Boolean> {
        val registerLiveData = MutableLiveData<Boolean>()
        val client = ApiConfig().getApiService().register(registerRequest)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("message", responseBody.message)
                        registerLiveData.value = true
                        return
                    }
                }
                registerLiveData.value = false
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                registerLiveData.value = false
            }
        })
        return registerLiveData
    }
}