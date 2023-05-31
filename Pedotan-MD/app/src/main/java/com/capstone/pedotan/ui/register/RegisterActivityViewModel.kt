package com.capstone.pedotan.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.request.RegisterRequest
import com.capstone.pedotan.model.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivityViewModel (private val repository: SettingsRepository) : ViewModel() {

    fun register(registerRequest: RegisterRequest): LiveData<Boolean> {
        val registerLiveData = MutableLiveData<Boolean>()
        val client = ApiConfig().getApiService().register(registerRequest)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.data
                    if (responseBody != null) {
//                        Log.d("message", responseBody.accessToken)
//                        showToast(response.)
                        registerLiveData.value = true
                        return
                    }
                }
//                showToast(response.message())
                registerLiveData.value = false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                t.message?.let { showToast(it) }
                registerLiveData.value = false
            }
        })
        return registerLiveData
    }

//    fun showToast(message: String) {
//        val toast: Toast = Toast.makeText(, message, Toast.LENGTH_SHORT)
//        toast.show()
//    }
}