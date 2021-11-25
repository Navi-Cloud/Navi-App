package com.navi.file.repository.server.user

import com.navi.file.model.UserLoginRequest
import com.navi.file.model.UserLoginResponse
import com.navi.file.model.UserRegisterRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/api/v1/user/join")
    fun registerUser(@Body userRegisterRequest: UserRegisterRequest): Call<ResponseBody>

    @POST("/api/v1/user/login")
    fun loginUser(@Body userLoginRequest: UserLoginRequest): Call<UserLoginResponse>
}