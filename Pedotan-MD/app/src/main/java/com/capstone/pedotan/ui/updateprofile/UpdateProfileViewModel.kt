package com.capstone.pedotan.ui.updateprofile

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.capstone.pedotan.api.ApiConfig
import com.capstone.pedotan.data.SettingsRepository
import com.capstone.pedotan.model.response.DiseaseResponse
import com.capstone.pedotan.model.response.FileUploadResponse
import com.capstone.pedotan.ui.profile.ProfileActivity
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

class UpdateProfileViewModel(private val repository: SettingsRepository) : ViewModel() {
    fun getSettings() : Settings {
        return repository.getSettings()
    }

    fun updateProfile(token: String, file: File, name: String, email: String, noHandphone: String, nik: String, location: String, callback: (FileUploadResponse?) -> Unit) {
        val pName = name.toRequestBody("text/plain".toMediaType())
        val pEmail = email.toRequestBody("text/plain".toMediaType())
        val pNoHandphone = noHandphone.toRequestBody("text/plain".toMediaType())
        val pNik = nik.toRequestBody("text/plain".toMediaType())
        val pLocation = location.toRequestBody("text/plain".toMediaType())

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val service = ApiConfig().getApiService().updateProfile(token, imageMultipart, pName, pEmail, pNoHandphone, pNik, pLocation)
        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    callback(responseBody)
                } else {
                    callback(null)
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}