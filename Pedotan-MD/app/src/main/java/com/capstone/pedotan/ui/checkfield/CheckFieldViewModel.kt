package com.capstone.pedotan.ui.checkfield

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.request.AddFieldRequest
import com.capstone.pedotan.model.request.CheckFieldRequest
import com.capstone.pedotan.model.response.FieldResponse
import com.capstone.pedotan.model.response.FileUploadResponse
import com.capstone.pedotan.model.response.NPKResponse
import com.capstone.pedotan.model.response.PredictResponse
import com.capstone.pedotan.ui.reduceFileImage
import com.senpro.ulamsae.model.Settings
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CheckFieldViewModel (private val repository: SettingsRepository) : ViewModel() {

    fun getSettings() : Settings {
        return repository.getSettings()
    }

    fun checkField(token: String, checkFieldRequest: CheckFieldRequest): LiveData<Boolean> {
        val loginLiveData = MutableLiveData<Boolean>()
        val client = ApiConfig().getApiService().checkField(token, checkFieldRequest)
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        loginLiveData.value = true
                        return
                    }
                }
                loginLiveData.value = false
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                loginLiveData.value = false
            }
        })
        return loginLiveData
    }

    fun uploadImage(getFile: File, callback: (NPKResponse?) -> Unit) {
        if (getFile != null) {
            val file = reduceFileImage(getFile)

            val requestImageImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageImage
            )

            val service = ApiConfig().getApiService().checkNPK(imageMultipart)
            service.enqueue(object : Callback<NPKResponse> {
                override fun onResponse(
                    call: Call<NPKResponse>,
                    response: Response<NPKResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        callback(responseBody)
                    } else {
                        Log.e("checkfield", response.message())
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<NPKResponse>, t: Throwable) {
                    callback(null)
                }
            })
        } else {
            callback(null)
        }
    }


//    fun showToast(message: String) {
//        val toast: Toast = Toast.makeText(, message, Toast.LENGTH_SHORT)
//        toast.show()
//    }
}