package com.capstone.pedotan.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.request.LoginRequest
import com.capstone.pedotan.model.request.RegisterRequest
import com.capstone.pedotan.model.response.LoginResponse
import com.capstone.pedotan.model.response.FileUploadResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivityViewModel(private val repository: SettingsRepository) : ViewModel() {

    fun saveSession(token: String, email: String) {
        repository.setLogin(true, token, email)
    }

    fun login(loginRequest: LoginRequest): LiveData<Boolean> {
        val loginLiveData = MutableLiveData<Boolean>()
        val client = ApiConfig().getApiService().login(loginRequest)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("message", responseBody.token)
                        saveSession(responseBody.token, loginRequest.email)
                        loginLiveData.value = true
                        return
                    }
                }
                loginLiveData.value = false
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginLiveData.value = false
            }
        })
        return loginLiveData
    }

    fun register(registerRequest: RegisterRequest): LiveData<Boolean> {
        val registerLiveData = MutableLiveData<Boolean>()
        val client = ApiConfig().getApiService().registerGoogle(registerRequest)
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