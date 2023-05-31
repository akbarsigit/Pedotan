package com.capstone.pedotan.ui.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.request.LoginRequest
import com.capstone.pedotan.model.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivityViewModel(private val repository: SettingsRepository) : ViewModel() {

    fun saveSession(token: String, refToken: String, id: String) {
        repository.setLogin(true, token, refToken, id)
    }

    fun login(loginRequest: LoginRequest): LiveData<Boolean> {
        val loginLiveData = MutableLiveData<Boolean>()
        val client = ApiConfig().getApiService().login(loginRequest)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.data
                    if (responseBody != null) {
//                        Log.d("message", responseBody.accessToken)
//                        showToast(response.)
                        saveSession(responseBody.accessToken, responseBody.refreshToken, responseBody.id)
                        loginLiveData.value = true
                        return
                    }
                }
//                showToast(response.message())
                loginLiveData.value = false
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                t.message?.let { showToast(it) }
                loginLiveData.value = false
            }
        })
        return loginLiveData
    }

//    fun showToast(message: String) {
//        val toast: Toast = Toast.makeText(, message, Toast.LENGTH_SHORT)
//        toast.show()
//    }
}