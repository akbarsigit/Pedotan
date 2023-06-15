package com.capstone.pedotan.ui.camera

import android.util.Log
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.response.DiseaseResponse
import com.capstone.pedotan.model.response.NPKResponse
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

class CameraActivityViewModel (private val repository: SettingsRepository) : ViewModel() {

    fun getSettings() : Settings {
        return repository.getSettings()
    }

    fun uploadImage(token: String, email: String, fieldId: String, getFile: File, callback: (DiseaseResponse?) -> Unit) {
        if (getFile != null) {
            val file = reduceFileImage(getFile)
            val pEmail = email.toRequestBody("text/plain".toMediaType())
            val pFieldId = fieldId.toRequestBody("text/plain".toMediaType())
            val requestImageImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageImage
            )

            val service = ApiConfig().getApiService().checkDisease(token, imageMultipart, pEmail, pFieldId)
            service.enqueue(object : Callback<DiseaseResponse> {
                override fun onResponse(
                    call: Call<DiseaseResponse>,
                    response: Response<DiseaseResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        callback(responseBody)
                    } else {
//                        Log.e("checkfield", response.message())
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<DiseaseResponse>, t: Throwable) {
                    callback(null)
                }
            })
        } else {
            callback(null)
        }
    }
}