package com.capstone.pedotan.api

import com.capstone.pedotan.model.request.LoginRequest
import com.capstone.pedotan.model.request.RegisterRequest
import com.capstone.pedotan.model.response.LoginResponse
import com.capstone.pedotan.model.response.RegisterResponse
import com.capstone.pedotan.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("authentications")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("users")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @GET("user/{id}")
    fun getUserDetail(
        @Path("id") query: String
    ): Call<UserResponse>
}