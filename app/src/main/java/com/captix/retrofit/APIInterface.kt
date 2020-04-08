package com.captix.retrofit

import com.captix.http_requests.login.LoginRequest
import com.captix.http_requests.login.LoginResponse
import com.captix.http_requests.photo_request.PhotoURLResponse
import com.captix.http_requests.registration.Registration
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface APIService {
    @POST("users")
    fun registrationPost(@Body registration: Registration): Call<Registration>

    @POST("authenticate")
    fun logInPost(@Body logIn: LoginRequest): Call<LoginResponse>

    @POST("authenticate")
    //@FormUrlEncoded
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("images")
    fun getPhotosURL(): Call<List<PhotoURLResponse>>

}

object ApiUtils {

    const val BASE_URL = "http://18.184.252.89:8080/"

    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(
            APIService::class.java
        )
}