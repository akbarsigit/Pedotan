package com.capstone.pedotan.api

import com.capstone.pedotan.model.request.AddFieldRequest
import com.capstone.pedotan.model.request.CheckFieldRequest
import com.capstone.pedotan.model.request.LoginRequest
import com.capstone.pedotan.model.request.RegisterRequest
import com.capstone.pedotan.model.response.DiseaseResponse
import com.capstone.pedotan.model.response.FieldResponse
import com.capstone.pedotan.model.response.LoginResponse
import com.capstone.pedotan.model.response.FileUploadResponse
import com.capstone.pedotan.model.response.NPKResponse
import com.capstone.pedotan.model.response.PredictResponse
import com.capstone.pedotan.model.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("auth/register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<FileUploadResponse>

    @POST("auth/google")
    fun registerGoogle(
        @Body registerRequest: RegisterRequest
    ): Call<FileUploadResponse>

    @Multipart
    @POST("auth/datauser")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("noHandphone") noHandphone: RequestBody,
        @Part("nik") nik: RequestBody,
        @Part("location") location: RequestBody,
    ): Call<FileUploadResponse>

    @POST("auth/farmdata")
    fun addField(
        @Header("Authorization") token: String,
        @Body addFieldRequest: AddFieldRequest
    ): Call<FileUploadResponse>

    @POST("ai/predictcrop")
    fun checkField(
        @Header("Authorization") token: String,
        @Body checkFieldRequest: CheckFieldRequest
    ): Call<PredictResponse>

    @Multipart
    @POST("ai/predictnpk")
    fun checkNPK(
        @Part file: MultipartBody.Part,
    ): Call<NPKResponse>

    @Multipart
    @POST("ai/predictdisease")
    fun checkDisease(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("email") email: RequestBody,
        @Part("farm_id") farm_id: RequestBody,
    ): Call<DiseaseResponse>

    @GET("auth/datauser")
    fun getUserDetail(
        @Header("Authorization") token: String,
        @Query("email") email: String,
    ): Call<UserResponse>

    @GET("auth/farmdata")
    fun getFieldsData(
        @Header("Authorization") token: String,
        @Query("email") email: String,
    ): Call<ArrayList<FieldResponse>>
}